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

En nuestro archivo XML, añadir un nuevo objeto que mostrará la imágen proporcionada por una URL
 Nombrandola con un ID denominado iVURLImage
    
````
        <ImageView
            android:id="@+id/ivURLImage"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:src="@drawable/ic_launcher_foreground" />

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

![](https://github.com/caamaledgar/documentationProjects/blob/main/RecyclerView/ImageApp.png)


Tambien es necesario que una vex capturada y registrada la informaión nuestos datos de captura inicial se limpien, para permitir una nueva captura.

````
 
        binding.tilNombre.getEditText().setText("");
        binding.tilCorreo.getEditText().setText("");
        binding.tilImagen.getEditText().setText("");
        binding.tilNombre.requestFocus();
````

Ahora vamos a refactorizar nuestro proyecto como nos marcan las mejores prácticas de la programación, para ellos crearemos nuevos métodos de nos dividan nuestra carga de trabajo. Para ello lo que hemos construido en nuestro OnViewCreated, cuando le damos click al boton lo vamos a enviar a nuevo método.

 ````
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertarRegistro(myRef);

            }
        });
 ````
Nuestro método va recibir como paràmetro nuestra refeencia de la base de datos por ello en nuestro método hay que declararlo.
Nuestro médodo debe quedar como sigue:

````
    public void insertarRegistro(DatabaseReference myRef){
        String nombre = binding.tilNombre.getEditText().getText().toString();
        String correo = binding.tilCorreo.getEditText().getText().toString();
        String imagen = binding.tilImagen.getEditText().getText().toString();
        //String imagen = "";
        Usuarios usuarios = new Usuarios(myRef.push().getKey(), nombre, correo,imagen);
        myRef.child(usuarios.getId()).setValue(usuarios);
        binding.ivURLImage.setVisibility(View.VISIBLE);
        Glide.with(binding.ivURLImage.getContext())
                .load(imagen)
                .error(R.drawable.ic_launcher_foreground)
                .apply(new RequestOptions().override(300, 300))
                .centerCrop()
                .circleCrop()
                .into(binding.ivURLImage);
        binding.tilNombre.getEditText().setText("");
        binding.tilCorreo.getEditText().setText("");
        binding.tilImagen.getEditText().setText("");
        binding.tilNombre.requestFocus();
    }
````

Continuamos refactorizando, siguiendo los principios SOLID, divideremos la funcionalida, un metodo para traer los registros y otro para limpiar en la pantalla los datos mostrados. Crearemos un método para mostrar la imágen.

````
    public void mostrarImagen(String imagen){
        binding.ivURLImage.setVisibility(View.VISIBLE);
            Glide.with(binding.ivURLImage.getContext())
                    .load(imagen)
                    .error(R.drawable.ic_launcher_foreground)
                    .apply(new RequestOptions().override(300, 300))
                    .centerCrop()
                    .circleCrop()
                    .into(binding.ivURLImage);
    }
````

Y lo mandamos a llamar desde nuestro onViewCreated, es necesario pasarle como parámetro la imagen 
````
    public void insertarRegistro(DatabaseReference myRef){
        String nombre = binding.tilNombre.getEditText().getText().toString();
        String correo = binding.tilCorreo.getEditText().getText().toString();
        String imagen = binding.tilImagen.getEditText().getText().toString();
        //String imagen = "";
        Usuarios usuarios = new Usuarios(myRef.push().getKey(), nombre, correo,imagen);
        myRef.child(usuarios.getId()).setValue(usuarios);
        mostrarImagen(imagen);
        binding.tilNombre.getEditText().setText("");
        binding.tilCorreo.getEditText().setText("");
        binding.tilImagen.getEditText().setText("");
        binding.tilNombre.requestFocus();
    }
    
````

Ya como último paso debemos de limpiar los campos de captura
Para ello creamos un método de limpieza
````
    public void limpiarCaptura(){
        binding.tilNombre.getEditText().setText("");
        binding.tilCorreo.getEditText().setText("");
        binding.tilImagen.getEditText().setText("");
        binding.tilNombre.requestFocus();
    }
````

Y lo mandamos a llamar desde nuestra función principal, que debe quedar con este esquema.
````
    public void insertarRegistro(DatabaseReference myRef){
        String nombre = binding.tilNombre.getEditText().getText().toString();
        String correo = binding.tilCorreo.getEditText().getText().toString();
        String imagen = binding.tilImagen.getEditText().getText().toString();
        //String imagen = "";
        Usuarios usuarios = new Usuarios(myRef.push().getKey(), nombre, correo,imagen);
        myRef.child(usuarios.getId()).setValue(usuarios);
        mostrarImagen(imagen);
        limpiarCaptura();
    }
````

Nuestro Proyecto ahora ya cuenta con la conexión a FireBase y realiza registros a la Base de Datos, el siguiente paso es realizar un proceso de validación para que no se inserten registros duplicados.

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

![](https://github.com/caamaledgar/documentationProjects/blob/main/RecyclerView/Packages.png)



Crear nuestra Adaptador
Inicialmente debemos de extender nuestra clase  de la clase RecyclerView

````
public class MyRegistrosAdapter extends RecyclerView.Adapter<MyRegistrosAdapter.MyViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder{

   }
}
````

Posteriormente, generamos el constructor del la clase MyViewHolder

````
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

````

A nuestra clase princpal le generamos sus métodos
````
public class MyRegistrosAdapter extends RecyclerView.Adapter<MyRegistrosAdapter.MyViewHolder> {

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}

````

De igual forma a nuestro constructor le añadimos dos atributos, un Context y un ArrayList
Con el generador de código le creamos un constructor, para estos atributos

````
public class MyRegistrosAdapter extends RecyclerView.Adapter<MyRegistrosAdapter.MyViewHolder> {
    
    Context context;
    ArrayList<Usuarios> list;

    public MyRegistrosAdapter(Context context, ArrayList<Usuarios> list) {
        this.context = context;
        this.list = list;
    }

````

Ya podemos modificar método getItemCount, por que ya tenemos un ArrayList
````
    @Override
    public int getItemCount() {
        //return 0;
        return list.size();
    }
````





La versión final debe mostrrnos una lista con nuestros registros incluyendo el diseño que creamos para cada item.

RecyclerViewLista
![](https://github.com/caamaledgar/documentationProjects/blob/main/RecyclerView/RecyclerViewLista.png)


