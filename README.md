# Oe - Générateur de site statique

---

## Manuel d'utilisateur

### Installation :

Décompresser le fichier [`projet-oe.zip`](https://github.com/gen-classroom/projet-oe/releases) et ajouter le dossier bin à votre `path`  avec la commande suivante :

```bash
export PATH=$PATH:`pwd`/projet-oe/bin
```

L'exécution de la commande `oe` devrait rendre le résultat suivant :

```bash
$ oe
Usage: projet-oe [-v] [COMMAND]
A brand new static site generator.
  -v, --version   version of oe static site generator
Commands:
  init   Init a static site
  clean  Clean a static site
  build  Build a static site
  serve  Serve a static site
```

### Détails :

#### Version :

L'option `-v` ou `--version` permettent de récupérer la version du programme :

```bash
$ oe -v
oe - v1.0.0
```

#### Initialiser un nouveau site :

L'argument **`init`** suivi du chemin (relatif/absolu) vers le dossier du site permet d'initialiser un nouveau site dans ce dossier :

```bash
$ oe init ./site
```

La commande va créer la structure du site dans le dossier spécifié.

```
site
├── config.json
├── index.md
├── pages
└── templates
```

- un fichier de configuration `config.json` contenant les configurations du site.

- un fichier `index.md` à remplir qui sera la page principale du site.
- un sous-dossier `pages` qui devra contenir toutes les [pages en format Markdown](#page) .

- un sous-dossier `templates` contenant les templates en format `HTML`.

#### Construire le site :

L'argument **`build`** suivi du chemin (relatif/absolu) vers le dossier du site permet de construire le nouveau site à partir des pages en Markdown rajoutée :

```bash
$ oe build ./site
```

La commande va créer un sous-dossier `build` dans le dossier du site, ce dossier contiendra l'index du site ainsi qu'un sous-dossier contenant toutes les pages créées en format `HTML`. Tous autres fichier non-markdown dans le dossier `pages`, seront simplement copiés.

```
site
├── build
│   ├── index.html
│   └── pages
├── config.json
├── index.md
├── pages
└── templates
```

##### Option disponible : 

- `-w` ou `--watch` : permet de mettre à jour le site automatiquement lorsque le contenu d'une page (markdown) est modifié.

#### Nettoyer un site :

L'argument **`clean`**  suivi du chemin (relatif/absolu) vers le dossier du site permet de supprimer le sous-dossier `build` du site :

```bash
$ oe clean ./site
```

#### Lancer un serveur pour le site :

L'argument **`serve`** suivi du chemin (relatif/absolu) vers le dossier du site permet de démarrer un serveur en local pour visionner le contenu du site dans un navigateur. 

```bash
$ oe serve ./site
```

Le site s'accède à l'adresse : [localhost:8080/index.html](localhost:8080/index.html).

##### Option disponible : 

- `-w` ou `--watch` : permet de mettre à jour le site automatiquement lorsque le contenu d'une page (markdown) est modifié.

### <a name="page"></a> Formatage des pages :

Les pages Markdown doivent respecter la structure suivante :

```
titre: [titre de la page]
auteur: [nom de l'auteur]
date: [date de rédaction]
---
[contenu de la page en format Markdown]
```

*Note : remplacer les [...] par le contenu voulu.
