# My_Application
Mi primera aplicación con Android Firebase - Fragments y RecyclerView

Nuestro Proyecto ahora ya cuenta con la conexión a FireBase y realiza registros a la Base de Datoa, el siguiente paso es realizar un proceso de validación para que no se inserten registros duplicados.

Para ello vamos añadir una funcionalidad a nuestra aplicación, utilizando la función Query de Firebasa


 ````
        Query userEmailQery = userRef.orderByChild("correo").equalTo(correo).limitToFirst(1);
        userEmailQery.addListenerForSingleValueEvent(new ValueEventListener() {
 ````


