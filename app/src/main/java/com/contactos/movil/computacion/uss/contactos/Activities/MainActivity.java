package com.contactos.movil.computacion.uss.contactos.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.contactos.movil.computacion.uss.contactos.Adapters.AdapterContacto;
import com.contactos.movil.computacion.uss.contactos.DAO.DaoContacto;
import com.contactos.movil.computacion.uss.contactos.Modelo.Contacto;
import com.contactos.movil.computacion.uss.contactos.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String CARPETA_RAIZ="misImagenesPrueba/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"misFotos";
    private Toolbar toolbar;

    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    ImageView botonCargar;
    ImageView imagen;
    String path;

    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    EditText etNombre, etTelefono, etEmail;
    Button btnRegistrar;

    DaoContacto daocontacto;
    List<Contacto> contactos;
    ListView listViewContactos;
    MenuItem mSearchAction;
    boolean isSearchOpened = false;
    EditText edtSeach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imagen=(ImageView)findViewById(R.id.imagenConbtacto1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        daocontacto = new DaoContacto(this);
        if (daocontacto.getAllStudentsList().size() == 0) {
            daocontacto.addContactoDetail(new Contacto("Bryan Callirgos Guimarey", "958481526", "bryan@gmail.com",imagen));
            daocontacto.addContactoDetail(new Contacto("Tania Tafur Callirgos", "958562458", "tania@gmail.com",imagen));
            daocontacto.addContactoDetail(new Contacto("Kevin Guevara Cabrera", "986215478", "kevin@gmail.com",imagen));
        }
        contactos = new ArrayList<>();
        listViewContactos = (ListView) findViewById(R.id.listViewContactos);
        listViewContactos.setLongClickable(true);
        Listar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        listViewContactos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int pos, long arg3) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Eliminar")
                        .setMessage("¿Desea eliminar este contacto?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Contacto contacto = (Contacto) listViewContactos.getItemAtPosition(pos);
                                daocontacto.deleteEntry(contacto.id);
                                Listar();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(R.drawable.alert)
                        .show();
                return true;
            }
        });

        listViewContactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contacto contacto = (Contacto) listViewContactos.getItemAtPosition(position);
                Intent i = new Intent(MainActivity.this, ContactoActivity.class);
                i.putExtra("id", contacto.getId());
                i.putExtra("nombre", contacto.getNombre());
                i.putExtra("phone", contacto.getTelefono());
                i.putExtra("email", contacto.getEmail());
                startActivity(i);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                LayoutInflater inflater = getLayoutInflater();
                final View dialoglayout = inflater.inflate(R.layout.dialog, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(dialoglayout);

                etNombre = (EditText) dialoglayout.findViewById(R.id.etNombre);
                etTelefono = (EditText) dialoglayout.findViewById(R.id.etTelefono);
                etEmail = (EditText) dialoglayout.findViewById(R.id.etEmail);
                btnRegistrar = (Button) dialoglayout.findViewById(R.id.btnRegistrar);
                btnRegistrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!etNombre.getText().toString().equals("") &&
                                !etTelefono.getText().toString().equals("") &&
                                !etEmail.getText().toString().equals("")) {

                            if (etEmail.getText().toString().matches(PATTERN_EMAIL)) {
                                Contacto contacto = new Contacto(etNombre.getText().toString(), etTelefono.getText().toString(), etEmail.getText().toString(),imagen);
                                daocontacto.addContactoDetail(contacto);
                                Listar();
                                Toast.makeText(MainActivity.this, "Registrado Correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Email incorrecto", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "Ingreso los datos requeridos", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.show();
            }
        });
    }

    private void Listar() {
        contactos = daocontacto.getAllStudentsList();
        AdapterContacto adapterMovimiento = new AdapterContacto(MainActivity.this, contactos);
        listViewContactos.setAdapter(adapterMovimiento);
    }

    private void Listar(String name) {
        contactos = daocontacto.searchContact(name);
        AdapterContacto adapterMovimiento = new AdapterContacto(MainActivity.this, contactos);
        listViewContactos.setAdapter(adapterMovimiento);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_buscar);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_buscar:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch() {
        ActionBar action = getSupportActionBar();

        if (isSearchOpened) {

            action.setDisplayShowCustomEnabled(false);
            action.setDisplayShowTitleEnabled(true);

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            mSearchAction.setIcon(getResources().getDrawable(R.drawable.search));

            isSearchOpened = false;
            Listar();
        } else {

            action.setDisplayShowCustomEnabled(true);
            action.setCustomView(R.layout.search_bar);
            action.setDisplayShowTitleEnabled(false);

            edtSeach = (EditText) action.getCustomView().findViewById(R.id.edtSearch);

            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    Listar(edtSeach.getText().toString());
                    return true;
                }
            });

            edtSeach.requestFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);

            mSearchAction.setIcon(getResources().getDrawable(R.drawable.close));

            isSearchOpened = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (isSearchOpened) {
            handleMenuSearch();
            return;
        }
        super.onBackPressed();
    }

    public void onclick(View view) {
        cargarImagen();
    }

    private void cargarImagen() {

        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final android.support.v7.app.AlertDialog.Builder alertOpciones=new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")){
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();

    }

    private void tomarFotografia() {
        File fileImagen=new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada=fileImagen.exists();
        String nombreImagen="";
        if(isCreada==false){
            isCreada=fileImagen.mkdirs();
        }

        if(isCreada==true){
            nombreImagen=(System.currentTimeMillis()/1000)+".jpg";
        }


        path=Environment.getExternalStorageDirectory()+
                File.separator+RUTA_IMAGEN+File.separator+nombreImagen;

        File imagen=new File(path);

        Intent intent=null;
        intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ////


        String authorities=getApplicationContext().getPackageName()+".provider";
        Uri imageUri= FileProvider.getUriForFile(this,authorities,imagen);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);


        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));

        startActivityForResult(intent,COD_FOTO);

        ////
    }

    private void doSearch() {

    }
}
