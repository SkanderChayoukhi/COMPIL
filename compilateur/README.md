# Compilateur PCF vers WebAssembly

## Auteurs

**Skander CHAYOUKHI**

**Siwar Ben Gharsallah**

**⚠️ Utilisation d'outils d'IA générative :**

- **ChatGPT** : utilisé pour la compréhension des concepts WebAssembly et la génération de certaines fonctions auxiliaires et debugging , aussi pour rédiger ce readme .

---

## Description du projet

Ce projet implémente un compilateur pour le langage PCF (Programming Computable Functions) vers WebAssembly Text Format (WAT). Le compilateur suit une architecture en plusieurs passes :

1. **Analyse lexicale et syntaxique** (ANTLR)
2. **Vérification de types** (algorithme d'unification)
3. **Génération de code intermédiaire** (machine abstraite)
4. **Génération de code WAT** (WebAssembly)

---

## Choix de conception et déviations

### 1. Machine abstraite sans accumulateur (Conforme TP)

**Choix :** Implémentation d'une machine à pile pure, sans accumulateur.

**Justification :** Ce choix suit strictement les consignes du TP1 pour se rapprocher de la machine abstraite de WebAssembly. La signature de `execute` est :

```scala
private def execute(
  s: List[Value],           // pile de valeurs
  env_stack: List[(List[Value], List[Ins])],  // pile d'environnements
  e: List[Value],           // environnement courant
  c: List[Ins]             // code restant
): Value
```

**Différences avec le cours :**

- Suppression de l'instruction `Push` redondante dans certains cas
- Toutes les opérations binaires dépilent deux valeurs et empilent le résultat

---

### 2. Instruction PopEnv modifiée

**Déviation majeure :** L'implémentation de `PopEnv` préserve la valeur en sommet de pile.

**Implémentation :**

```scala
case (v::s1, (savedEnv, _)::envStack1, _, Popenv::c1) =>
  execute(v::s1, envStack1, savedEnv, c1)
```

**Justification :** Pour compiler un `let`, après l'exécution du corps, la valeur résultat doit rester en sommet de pile tandis que l'environnement sauvegardé (qui n'est pas en sommet de pile) doit être restauré.

**Code WAT correspondant :**

```wasm
(func $popenv
  (local $val i32)
  (local.set $val (call $pop))      ;; sauver la valeur
  (global.set $ep (call $pop))      ;; restaurer l'environnement
  (call $push (local.get $val)))    ;; remettre la valeur sur la pile
```

---

### 3. Gestion des fermetures avec index

**Choix :** Ajout d'un champ `idx: Int` dans les instructions `Mkclos` et `Mkfixclos`.

**Structure :**

```scala
case class Mkclos(idx: Int, code: List[Ins]) extends Ins
case class Mkfixclos(idx: Int, code: List[Ins]) extends Ins
```

**Justification :** WebAssembly ne permet pas de manipuler du code comme données. Les fermetures sont donc implémentées comme des paires `(index, environnement)` où l'index pointe vers une fonction dans la table WAT.

**Méthode de génération des index :** Approche fonctionnelle avec propagation de l'index :

```scala
def genAM(aterm: ATerm, idx: Int): (Code, Int) = {
  // retourne (code généré, prochain index disponible)
}
```

---

### 4. Fonction $apply avec paramètres explicites

**Choix :** La fonction `$apply` WAT prend deux paramètres au lieu de dépiler.

**Signature WAT :**

```wasm
(func $apply (param $arg i32) (param $closure i32) (result i32)
  (local $idx i32)
  (local $env i32)
  (local.set $idx (call $fst (local.get $closure)))
  (local.set $env (call $snd (local.get $closure)))
  (global.set $ep (local.get $env))
  (local.get $arg)
  (call $extend)
  (call_indirect (type 0) (local.get $idx)))
```

**Justification :** Cette approche est plus claire et évite des manipulations de pile complexes en WAT. L'instruction `Apply` en machine abstraite empile déjà l'argument dans l'environnement.

---

## État d'avancement

### ✅ Ce qui fonctionne

#### PCF Vert (expressions arithmétiques)

- ✅ Constantes entières (`Ldi`)
- ✅ Opérations arithmétiques : `Add`, `Sub`, `Mul`, `Div`
- ✅ Conditionnelles : `Test` (if-then-else)
- ✅ Tous les tests green0 à green9 passent

#### PCF Bleu (variables et environnements)

- ✅ Variables liées : `Search(n)`
- ✅ Constructions `let` : `Pushenv`, `Extend`, `Popenv`
- ✅ Environnements imbriqués
- ✅ Tests blue0 à blue7, blue10 réussis
- ⚠️ **Échec :** blue8 et blue9 (variables non liées, `index = -1`)

#### PCF Rouge (fonctions non récursives)

- ✅ Création de fermetures : `Mkclos`
- ✅ Application de fonctions : `Apply`
- ✅ Fermetures capturant l'environnement
- ✅ Fonctions d'ordre supérieur
- ✅ Tests red0 à red6, red13 à red19, red40 à red43 réussis
- ⚠️ **Échec :** red7 (état VM inattendu)
- ⚠️ **Échec :** red11 (erreur de type attendue)

#### PCF Noir (récursivité)

- ✅ Fermetures récursives : `Mkfixclos`
- ✅ Fonctions récursives simples (factorielle)
- ✅ Tests black0, black1, black3 réussis
- ⚠️ **Échec :** black2 (résultat incorrect : attendu 12, obtenu 3)

#### Génération WAT

- ✅ Module WAT complet avec prélude
- ✅ Table des fonctions (`table funcref`, `elem`)
- ✅ Génération des fonctions de fermeture
- ✅ Instructions de base traduites correctement
- ✅ If-else avec `(result i32)`

### ❌ Ce qui ne fonctionne pas

#### 1. Variables libres (blue8, blue9)

**Problème :** Compilation échoue avec `Variable non liée (index = -1)`

**Cause probable :** La phase d'annotation ne détecte pas correctement les variables libres ou l'environnement de compilation ne les gère pas.

**Fichiers concernés :**

- `src/ast/Term.scala` (annotation)
- `src/generator/Generator.scala` (genAM)

#### 2. Test red7

**Problème :** `unexpected VM state`

**Programme concerné :** Probablement une application complexe de fonctions d'ordre supérieur.

**Cause probable :** Incompatibilité entre la pile d'environnements et la gestion des `Return`.

#### 3. Récursivité imbriquée (black2)

**Problème :** Résultat incorrect (3 au lieu de 12)

**Programme :** Addition récursive imbriquée (sumrec)

**Code généré :**

```scala
List(Pushenv, Mkfixclos(0,List(Mkclos(1,List(Search(1), Test(...))))), ...)
```

**Cause probable :** La fermeture récursive imbriquée ne capture pas correctement `$closure2` dans son environnement. Le `Search(2)` dans le corps interne ne pointe peut-être pas vers la bonne fermeture.

#### 4. Compilation WAT incomplète

**Manque :**

- Instructions `Mkclos` et `Mkfixclos` traduites en commentaires WAT au lieu de code fonctionnel
- Pas de gestion réelle des fermetures en WAT (seulement des placeholders)

---

## Corrections à apporter

### 1. Variables libres (blue8, blue9)

**Action :** Modifier `Term.annotate` pour détecter les variables hors contexte.

**Fichier :** `src/ast/Term.scala`

**Principe :**

```scala
case Var(x) =>
  val index = ctx.indexOf(x)
  if index == -1 then
    throw new Exception(s"Variable libre: $x")
  AVar(x, index, ctx)
```

**Test :** Vérifier que les variables utilisées sont bien dans `ctx` avant d'annoter.

---

### 2. Test red7

**Action :** Analyser le programme red7.pcf et tracer l'exécution pas à pas.

**Fichier :** `src/vm/VM.scala`

**Principe :**

- Ajouter des traces pour identifier l'état exact de la pile lors de l'erreur
- Vérifier que les `Return` correspondent bien aux `Apply`
- S'assurer que la pile d'environnements est correctement gérée

**Code de debug suggéré :**

```scala
case _ =>
  println(s"State: s=$s, env_stack=$env_stack, e=$e, c=$c")
  throw new RuntimeException(s"unexpected VM state")
```

---

### 3. Récursivité imbriquée (black2)

**Action :** Corriger la génération d'environnement pour `Mkfixclos`.

**Fichier :** `src/vm/VM.scala`

**Problème identifié :**

```scala
case (Value.RecClosure(code, closure_env)::arg::s1, _, _, Apply::c1) =>
  val recClos = Value.RecClosure(code, closure_env)
  execute(s1, (e, c1)::env_stack, arg::recClos::closure_env, code)
```

**Correction :** Pour une fermeture récursive imbriquée, il faut s'assurer que la fermeture externe est aussi accessible.

**Principe :**

- Lors de `Mkfixclos`, créer une fermeture qui se référence elle-même
- Vérifier que `closure_env` contient bien toutes les fermetures nécessaires

---

### 4. Génération WAT des fermetures

**Action :** Compléter `emitIns` pour `Mkclos` et `Mkfixclos`.

**Fichier :** `src/generator/GeneratorWasm.scala`

**Implémentation :**

```scala
case Mkclos(idx, _) =>
  List(
    WAT.Ins(s"i32.const $idx"),
    WAT.Ins("global.get $ep"),
    WAT.Ins("call $pair")
  )

case Mkfixclos(idx, _) =>
  // Créer une paire récursive (à définir en WAT)
  List(
    WAT.Ins(s"i32.const $idx"),
    WAT.Ins("global.get $ep"),
    WAT.Ins("call $pair")  // Simplifié, nécessite $mkfixclos
  )

case Apply =>
  List(WAT.Ins("call $apply"))
```

**Prérequis :** Ajouter une fonction `$mkfixclos` dans le prélude WAT pour gérer les fermetures récursives.

---

## Composants à modifier pour compléter

### 1. Prélude WAT

**Fichier :** `src/generator/GeneratorWasm.scala` (méthode `prelude`)

**Ajouts nécessaires :**

- Fonction `$mkfixclos` pour créer des fermetures récursives en WAT
- Gestion du garbage collection (optionnel)

### 2. Génération du code des fermetures

**Fichier :** `src/generator/GeneratorWasm.scala`

**Méthode à compléter :** `emitFunctions`

**Principe :**

- Chaque corps de fermeture devient une fonction WAT
- La fonction doit pouvoir accéder à l'environnement capturé via `$ep`

### 3. Gestion de la pile d'environnements

**Fichier :** `src/vm/VM.scala`

**Amélioration :**

- Vérifier la cohérence de `env_stack` lors des `Apply` et `Return`
- Ajouter des assertions pour détecter les incohérences

### 4. Tests et validation

**Fichiers :** `src/test/*.scala`

**Actions :**

- Créer des tests unitaires pour chaque instruction isolée
- Tester la génération WAT avec un interpréteur WAT (ex: wasmtime)
- Comparer les résultats VM et WAT pour chaque test

---

## Exécution et tests

### Lancer les tests

```bash
# Dans IntelliJ ou via sbt
sbt "runMain test.TestInterp"
```

### Résultats attendus

- **Green (10/10)** ✅
- **Blue (8/10)** ⚠️ (blue8, blue9 échouent)
- **Red (18/22)** ⚠️ (red7, red11 échouent)
- **Black (3/4)** ⚠️ (black2 incorrect)

### Génération WAT

Les tests affichent le code WAT généré pour chaque fichier `.pcf`. Exemple :

```
Fichier: green0.pcf
Code généré: List(Ldi(0))
Résultat VM: IntVal(0)
WAT généré: OK (1931 chars)
```

---

## Conclusion

Le compilateur implémente avec succès la compilation de PCF vert, bleu, rouge et noir vers une machine abstraite à pile pure, ainsi que la génération partielle de code WAT. Les principales limitations concernent :

1. La gestion des variables libres (détection lors de l'annotation)
2. Quelques cas complexes de récursivité imbriquée
3. La génération WAT complète (actuellement des placeholders)

Les corrections suggérées permettraient d'atteindre un compilateur pleinement fonctionnel vers WebAssembly.

---

**Date de dernière mise à jour :** 2025-12-19
