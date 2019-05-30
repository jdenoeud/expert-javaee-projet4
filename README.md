
| Branch        | Build           |
| ------------- |:-------------:|
| master      | [![Build Status](https://travis-ci.com/jdenoeud/expert-javaee-projet4.svg?token=3GQq85RKYqZzJeJutF7K&branch=master)](https://travis-ci.com/jdenoeud/expert-javaee-projet4) 

# Organisation des tests
* Les tests unitaires sont écrits dans les dossiers src/test/java des modules model et business.
* Les tests d’intégration sont écrits dans les dossiers src/test-consumer/java et src/test-business/java.

# Correction des erreurs de développement
La mise en place des tests unitaires et d’intégration a permis de mettre en évidence plusieurs erreurs dans le code :
*	Classe `EcritureComptable` --> la méthode `getTotalCredit()` faisait appel à vEcritureComptable.getDebit() au lieu de vEcritureComptable.getCredit().
-	Classe `EcritureComptable` --> j’ai remplacé `equals` par `compareTo()` car 2 valeurs peuvent être égale même si l’arrondi est différent.
-	Classe `EcritureComptable` --> erreur dans l’expression régulière permettant de valider le format de la référence d’une écriture comptable.
-	Fichier `sqlContext.xml` --> erreur de syntaxe dans la requête `SQLinsertListLigneEcritureComptable`.

# Modifications / Améliorations apportées
En plus de l’implémentations des TODOS, j’ai également apporté d’autres compléments / améliorations, parmi lesquels :
-	Dans la couche business, ajout de la méthode `getSoldeCompteComptable` permettant de retourner le solde d’un compte comptable et d’indiquer s’il est débiteur ou créditeur (RG_Compta_1).
-	Dans la couche model, ajout de la classe `SoldeCompteComptable`.
-	Dans la couche model, ajout de la classe `SequenceEcritureComptable`.
-	Dans la couche consumer, ajout du RowMapper `SequenceEcritureComptableRM` et des méthodes et requêtes SQL permettant de récupérer ou d’insérer une séquence d’écriture comptable en base de données.
-	Dans la couche business, création de la classe `ComptabiliteDaoMock` permettant de mocker l’appel à la couche consumer.
-	Dans la couche technical, configuration du fichier `log4j2.xml` pour tracer les requêtes SQL.

Not : cette liste de modifications n’est pas exhaustive.





