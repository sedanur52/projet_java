****** Compilation du projet *****


**Pour compiler**
javac Server.java 
Javac Client.java

**Pour executer** 
java Server  
Java Client


Le système permet  aux utilisateurs de s’inscrire et de se connecter et  de s’échanger des fichiers,Tous les fichiers d’un utilisateur sont transférer au répertoire du serveur et synchronisé avec les fichiers déjà présents dans le répertoire du serveur correspondant à l’ensemble des fichiers de  tous les utilisateurs. 

Nous avons au total 4 classes :

    • Une classe Server : 
      
      -accepte les demandes de connexions des clients et permet aux utilisateurs  de se connecter ou de s’inscrire en leur demandant d’entrer soit «INSCRIPTION» ou «CONNEXION » si autre chose que ces deux mots est reçu alors il leur est demandé de rentrer de nouveau  l’un des deux mots jusqu’à que ce soit le cas.
      Si le mot «INSCRIPTION» est reçu alors il est demandé au client d’entrer un identifiant puis un mot de passe qui tous les deux doivent avoir une taille inférieur ou égal à 20 caractères,sinon un message d’erreur est affiché et de meme si un utilisateur avec cet identifiant existe déjà. Si l’identifiant et le mot de passe entrés sont valides alors un objet User correspondant est crée et ajouté à la base de donnée (ici correspond à une liste de User).
      Si le mot «CONNEXION » est reçu  il est demandé au client d’entrer un identifiant puis un mot de passe puis il est vérifié si un utilisateur avec cet identifiant et mot de passe existe dans la database si une message de succès lui est envoyé sinon un message d’erreur.

	-a chaque fois q’un utilisateur est connecté le répertoire de l’utilsateur  est reçu dans le répertoire du serveur , dans lequel a déjà été crée un autre répertoire permettant la synchronisation avec les fichiers déjà présents dans le serveur et les fichiers qui viennent d’être reçus qui est ensuite envoyé au Client et qui remplacera le répertoire local de l’utilisateur.
      
    • Une classe Client : permet à l’utilisateur de s’inscrire ou de se connecter et synchronisé son répertoire locale avec le répertoire du serveur.
      
    • Une classe User : Défini les Users de notre système qui sont caractérisés par leur identifiant, mot de passe et un ID .

    • Une classe Rep : contient les fonctions permettant de manipuler les objets File java tels que la fonction permettant de synchroniser deux répertoire.



