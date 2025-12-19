# Fichiers WAT générés

Ce dossier contient les fichiers WAT générés par le compilateur PCF.

Les fichiers sont nommés selon le fichier source `.pcf` :
- `test1.wat`, `test2.wat`, etc. : tests simples
- `green0.wat`, `green1.wat`, etc. : compilation des tests verts
- `blue0.wat`, `blue1.wat`, etc. : compilation des tests bleus

Pour exécuter un fichier WAT :
```bash
wasmtime fichier.wat --invoke main
```

