# ift4001
Répertoire pour les travaux de Optimisation combinatoire

## TP1
Travail pratique _
IFT 4001 / IFT 7020 Optimisation Combinatoire _
Enseignant: Claude-Guy Quimper _
Session: Hiver 2016 _
Date de remise: 21 février 2016, 23h55 _
Équipes de 2 ou 3 personnes _

Premier problème
----------------
Le premier problème consiste à résoudre le jeu des quatre cubes. Vous avez quatre cubes
dont les faces sont colorées en jaune, en rouge, en bleu ou en vert. Vous devez aligner
les cubes côte à côte de sorte à former un prisme rectangulaire de dimension 1 x 1 x 4.
Les cubes doivent être disposés de façon à ce que les quatre couleurs apparaissent sur
chacune des quatre faces rectangulaires du prisme.
Voici la façon dont les cubes sont colorés. Pour construire le jeu, découpez le contour des
croix et pliez à angle de 90° le long des lignes. Vous obtiendrez les quatre cubes qu’il faut
aligner.



######Les livrables
* Le code de programmation. Un seul fichier .java devrait suffire pour cette question;
* Un document PDF comportant;
* Une page titre avec les noms et numéros de dossier de chaque membre de l’équipe;
* Une description détaillée et complète du modèle (variables, domaines, contraintes). Il
ne suffit pas de reproduire votre code de programmation ou de l’expliquer. Le lecteur
de votre rapport devrait comprendre votre modèle sans avoir jeté un coup d’oeil à
votre programme;
* Une analyse de votre modèle: le nombre de variables, le nombre de valeurs (par
domaine et au total), le nombre de contraintes et, s'il y a lieu, le nombre d'entrées
dans les contraintes tableau.
* La solution retournée par le solveur Choco 3;
* Le temps requis au solveur pour trouver votre solution;
* Le nombre de retours arrière effectués par le solveur.


Deuxième problème
-----------------
Le deuxième problème que vous devez résoudre est un problème d’optimisation. Vous
devez concevoir un horaire de travail pour les cinq employés d’une boutique. La boutique
est ouverte de 9h00 à 16h59. D’après la convention collective, un employé doit travailler
entre 5 et 7 heures par jour incluant une pause obligatoire de 30 minutes. La pause doit
être précédée et suivie d'une période de travail d'au moins deux heures. Un employé peut
donc travailler 2 heures, prendre une pause de 30 minutes et compléter sa journée en
travaillant 2h30 pour un total de 5 heures de présence au travail.
D’après les propriétaires de la boutique, le nombre d’employés travaillant pour chaque
période d'une demie-heure de la journée devrait fluctuer de la façon suivante.

En aucun cas la boutique ne doit être laissée sans employé. Le nombre d’employés
souhaité ne pourra pas toujours être atteint. On estime à * 20x * $ par demi-heure la perte de
profit lorsqu’il y a * x * employés de moins ou * x * employés de plus que le nombre souhaité. Il
vous faut donc trouver l’horaire de travail qui minimise les pertes de profit.
L’horaire suivant mène à une perte de profit de 180$. Cette solution n’est pas optimale.

Vous devez modéliser ce problème en utilisant les contraintes disponibles dans le solveur
Choco 3. Vous devez aussi fournir une analyse de votre modèle, c'est-à-dire le nombre de
variables, de contraintes et de valeurs dans les domaines en utilisant la notation ⇥ . Pour
votre analyse, supposez qu'il y a p périodes dans une journée et n employés et donnez le
nombre de contraintes, de variables, de valeurs et d'entrées dans les contraintes tableau
en fonction de p et de n. Dans l'instance que vous devez résoudre, nous avons p = 16 et
n = 5.

Vous devez implanter ce modèle en Java et le soumettre au solveur Choco 3. Si le solveur
prend trop de temps pour retourner une solution, considérez les options suivantes.
* Changez les heuristiques de recherche ou définissez les vôtres.
* Ajoutez une limite de 10 minutes au temps de résolution du solveur. Ainsi, vous
n’obtiendrez pas une solution optimale, mais probablement une bonne solution.
* Modifier votre modèle afin d’obtenir un filtrage plus fort. Un bon modèle a peu de
variables et l'affectation d’une variable à une valeur filtre beaucoup les autres domaines.
Indice: Les contraintes vues au chapitre 1, combinées aux méta-contraintes du chapitre 3,
sont suffisantes pour résoudre ce problème.
Les livrables
* Votre code de programmation (seulement les fichiers avec l'extension .java).
* Un document PDF contenant:
* Une description détaillée de votre modèle (variables, domaines, contraintes,
fonction objectif). Je dois pouvoir reproduire vos résultats seulement en lisant la
description de votre modèle;
* Une analyse de votre modèle;
* Une description des heuristiques utilisées (ou mentionnez que vous avez utilisé
les heuristique par défaut);
* La solution trouvée;
* Une mention justifiant si cette solution est optimale ou une mention disant que
vous n’avez pas réussi à prouver que cette solution est optimale. Un solveur ;
* Le temps requis au solveur pour trouver votre solution;
* Le nombre de retours arrière effectués par le solveur;

Références
----------
La page web du solveur Choco 3: 
* [http://choco-solver.org](http://choco-solver.org)
* Documentation de Choco 3:
[http://choco-solver.org/user_guide/](http://choco-solver.org/user_guide/)
* Les exemples de Choco
Décompressez le fichier « choco-solver-3.3.3-sources.jar » et aller dans le dossier org/
chocosolver/samples/. Vous trouverez des programmes qui utilisent une large variété de
problèmes.
