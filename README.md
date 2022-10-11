# My_Application
Mi primera aplicación con Android Firebase - Fragments y RecyclerView


El tratamiento de la imágen principal se realizó con la libreria Glide

En el archivo build.gradle de la app, se añade las dependencias de glide
    
````
dependencies{
    // Glide
    implementation 'com.github.bumptech.glide:glide:4.14.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.14.0'
}
````

Incluir en archivo AndroidManifiest.xml la funcionalidad para visualizar archivos desde Internet

````
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <uses-permission android:name="android.permission.INTERNET" />
````

Añadir la funcionalidad en nuestros fragments/activities para consumir imágenes desde una URL, añadir este proceso posterior a guardar los datos en Firebase

````
                    binding.ivURLImage.setVisibility(View.VISIBLE);
                    Glide.with(binding.ivURLImage.getContext())
                            .load(imagen)
                            .error(R.drawable.ic_launcher_foreground)
                            .apply(new RequestOptions().override(300, 300))
                            .centerCrop()
                            .into(binding.ivURLImage);

````


Nuestro Proyecto ahora ya cuenta con la conexión a FireBase y realiza registros a la Base de Datoa, el siguiente paso es realizar un proceso de validación para que no se inserten registros duplicados.

Para ello vamos añadir una funcionalidad a nuestra aplicación, utilizando la función Query de Firebasa


 ````
        Query userEmailQery = userRef.orderByChild("correo").equalTo(correo).limitToFirst(1);
        userEmailQery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                 //... TODO

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
 ````

A esta Listener validar que la consulta tiene indormación si el Query regresa datos, nos indicará que el registro ya se encuentra en nuestra base de datos.
En caso contrario mover nuestra funcionalidad actual para registro de la información.

 ````
        userEmailQery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(view.getContext(), "No se puede realizar el Registro. Correo Existente " + snapshot.toString(), Toast.LENGTH_SHORT).show();

                } else {
                    userRef.child(usuarios.getUid()).setValue(usuarios);
                    binding.inputNombre.getEditText().setText("");
                    binding.inputCorreo.getEditText().setText("");
                    binding.inputImagen.getEditText().setText("");
                    binding.inputNombre.requestFocus();
                    Toast.makeText(view.getContext(), "Registro de Usuario Exitoso", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "Registro "+usuarios.toString());

                    binding.ivURLImage.setVisibility(View.VISIBLE);
                    Glide.with(binding.ivURLImage.getContext())
                            .load(imagen)
                            .error(R.drawable.ic_launcher_foreground)
                            .apply(new RequestOptions().override(300, 300))
                            .centerCrop()
                            .into(binding.ivURLImage);
                }
            }                

 ````

Para iniciar con nuestro RecyclerView, debemos crear un nuevo fragmento donde se trabajará esta nueva vista, la cual la crearemos desde nuestra vista de navegación nav_graph

![](https://github.com/caamaledgar/documentationProjects/blob/main/RecyclerView/RecylerView.png)






