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

Añadir a este fragmento nuestro RecyclerView, desde Palettte/Common/RecyclerView

````
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvRegistros"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical" />
````

De igual forma crearmos un Layout para los detalles de nuestro RecyclerView, en el opción Nuevo/Layout Resource File desde el botón contextual posicionado en la carpeta Layout

![](https://github.com/caamaledgar/documentationProjects/blob/main/RecyclerView/detailregistro.png)

````
<androidx.cardview.widget.CardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:elevation="1dp"
    android:layout_margin="8dp"
    android:layout_height="wrap_content"
    app:cardElevation="10dp"
    app:cardCornerRadius="20dp"
    >

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="5dp"
        android:layout_margin="5dp">

        <ImageView
            android:id="@+id/imgItem"
            android:layout_width="50dp"
            android:layout_height="50dp"
            android:layout_weight="1"
            app:srcCompat="@drawable/ic_launcher_foreground" />


        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:layout_weight="2"
            android:padding="5dp">

            <TextView
                android:id="@+id/tvnombre"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:textStyle="bold"
                android:padding="5dp"
                android:layout_marginStart="5dp"
                android:textColor="@color/primaryColor"
                android:text="Nombre Completo"/>
            <TextView
                android:id="@+id/tvcorreo"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:padding="3dp"
                android:layout_marginStart="5dp"
                android:text="Correo Electrónico"/>
            <TextView
                android:id="@+id/tvimagen"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:padding="3dp"
                android:textSize="9dp"
                android:layout_marginStart="5dp"
                android:text="URL Imagen"/>
        </LinearLayout>
    </LinearLayout>

</androidx.cardview.widget.CardView>

````

![](https://github.com/caamaledgar/documentationProjects/blob/main/RecyclerView/detalleRegistro.png)


Ahora relacionaremos nuestro Fragmento de captura con nuestro RecyclerView, para ello incluyamos un botón de estilo TexButton, con un icono de flecha, que previamente debemos de incluir como un Vector Asset y añadirle un ID

````
    <Button
        android:id="@+id/btnRecyclerRegistros"
        style="@style/Widget.MaterialComponents.Button.TextButton"
        android:layout_width="36dp"
        android:layout_height="47dp"
        android:layout_marginEnd="16dp"
        android:text=""
        app:icon="@drawable/ic_baseline_arrow_forward_24"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

````

En nuestro archivo Java añadir la navegación

````
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        
        //...

        binding.btnRecyclerRegistros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(view.getContext(), "Click Fragmento", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.recyclerRegistrosFragment);
            }
        });

````
Crear dentro de nuestro proyecto en la carpeta Java y en el pakage com, dos packages:
1.- Adapter
2.- Recycler

Estos son los elementos junto con el RecyclerVier y el Layout del Item quienes darán funcionalidad a nuestro Recycler


La versión final debe mostrrnos una lista con nuestros registros incluyendo el diseño que creamos para cada item.

RecyclerViewLista
![](https://github.com/caamaledgar/documentationProjects/blob/main/RecyclerView/RecyclerViewLista.png)


