# 2026_compilation

## Pour commencer

Vous devez créer un “fork” du dépôt, avec le nom suivant : **2026_compilation_X_Y**, où X est le nom du premier membre du binôme et Y le nom du second membre du binôme, par exemple
_2026_compilation_macron_borne_.

Votre fork doit être PRIVE et vous devez ajouter comme membre en qualité de Maintainer votre responsable de TP.

Ensuite vous pourrez faire un clone du “nouveau” dépôt créé, où **monId** est votre identifiant etulab :


# Vous demandera votre login/mdp à chaque pull/push
```console
git clone https://etulab.univ-amu.fr/monId/2026_compilation_X_Y.git
```
# Ou alors, si votre clé SSH est bien configurée sur etulab
```console
git clone git@etulab.univ-amu.fr:monID/2026_compilation_X_Y.git
```

Pour donner le nom **squelette**, par exemple, au dépôt d’origine afin de garder le lien avec lui, on fait :

```console
git remote add squelette https://etulab.univ-amu.fr/nasr/2026_compilation
```

Ensuite, lors des TP suivants, vous pourrez mettre à jour votre dépôt en faisant :

```console
git pull squelette main
```

## Utilisation

### Chaque TP

Le fichier **Compiler.java** est le fichier principal qui pilote les différentes parties du compilateur que nous allons construire.

À chaque TP, nous décommenterons une ligne du fichier **Compiler.java** afin d'ajouter la fonctionnalité que nous avons développée.

Exemple TP1 :

```java
	public static void main(String[] args) {
		processCommandLine(args);
		System.out.println("[BUILD SC] ");
//		buildSc(); // <-- décommenter cette ligne
//		System.out.println("[BUILD SA] ");
```

### TP1
Objectif : programmer un analyseur lexical et un analyseur syntaxique pour le langage L. Les deux analyseurs sont produits automatiquement avec le logiciel **sablecc**, à partir d’un fichier de spécification **l.cfg**.

* Générer les analyseurs pour la grammaire l.cfg
```console
2025-compilation/src$ java -jar ../sablecc/sablecc.jar l.cfg
```

* Compiler le compilateur
```console
2025-compilation/src$ javac Compiler.java
```

* Générer les analyseurs **et** compiler le compilateur
```console
2025-compilation/src$ make
```

* Compiler (analyser) un fichier de test (un programme en L) avec le compilateur
```console
2025-compilation/src$ java Compiler ../test/input/add1.l
```

* Compiler et générer l'arbre de derivation
```console
2025-compilation/src$ java Compiler -v 2 ../test/input/add1.l
```

* Examiner l'arbre généré
```console
2025-compilation/src$ less ../test/input/add1.sc
```

* Nettoyer (en cas d'erreurs)
```console
2025-compilation/src$ make clean
```

* Lancer tous les tests (lancer le compilateur sur tous les programmes de test en L) et évaluer leur resultat

```console
2025-compilation/test$ python3 evaluate.py
```

### TP2

Objectif: construire un arbre abstrait correspondant à un programme en langage L.

* Compiler SaVM
```console
2025-compilation/src$ make
```

* Générer l'arbre abstrait pour un programme de test
```console
2025-compilation/src$ java Compiler -v 2 ../test/input/add1.l
```

* Tester l'arbre avec SaVM
```console
2025-compilation/src$ java -jar ../vm/SaVM.jar -sa ../test/input/add1.sa -v 1
```

### TP3

Objectif: construire, pour un programme donné, la table des symboles lui correspondant.


* Générer la table de symboles pour un programme de test
```console
2025-compilation/src$ java Compiler -v 2 ../test/input/add1.l
```

### TP5

Objectif: générer du code trois adresses à partir d’un arbre abstrait.

* Générer le code trois adresses pour un programme de test
```console
2025-compilation/src$ java Compiler -v 2 ../test/input/add1.l
```

* Tester le code 3 adresses (et la table de symboles) avec C3aVM

```console
2025-compilation/src$ java -jar ../vm/C3aVM.jar -c3a ../test/input/add1.c3a -ts ../test/input/add1.ts
```

### TP6

Objectif: générer du code pré-assembleur à partir du code trois adresses.

* Générer le code pre-nasm pour un programme de test
```console
2025-compilation/src$ java Compiler -v 2 ../test/input/add1.l
```

* Tester le code pre-nasm avec NasmVM

```console
2025-compilation/src$ java -jar ../vm/NasmVM.jar -nasm ../test/input/add1.pre-nasm
```

