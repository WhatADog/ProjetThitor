# ProjetGS15

Pour espérer un fonctionnement normal il faut :
- Disposer de java 8
- Lancer le programme avec la commande : "java VcesTrueRSA.class"

Pour CramerShoup : 
- Les fichiers générés lors de la génération des clés sont : "key.pub" pour les clés publiques et "key.prv" pour les clés privées
- Ne fonctionne qu'avec des fichiers de 128bits, la valeur étant définie en dur dans le code
- Un fichier de test est disponible et se nomme "csTest128b.txt"
- Le fichier chiffré se nomme "messageChiffreCS.txt", valeur entrée en dur dans le code

Pour le hashage : 
- Le hash du fichier est stocké dans le fichier "hash_sha512.txt", valeur entrée en dur dans le code

Pour ThreeFish :
- Cette partie rencontre des problèmes avec les caractères accentués. Un fichier de test assez long mais sans caractères accentués est disponible et se nomme "aChiffrer.txt", valeur entrée en dur dans le code
- Le message chiffré est stocké dans le fichier "Chiffré.txt", valeur entrée en dur dans le code
- Le message déchiffré est stocké dans le fichier "Déchiffré.txt", valeur entrée en dur dans le code
