package com.example.tp4;

import static com.example.tp4.R.id.editTextText;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;

import java.util.ArrayList;


public class MainActivity extends AppCompactActivity {
    private Button b1, b2;
    private EditText nom;
    private EditText mail;
    private int phone;
    DataBase dbb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        b1 = (Button) b1.findViewById(R.id.b1);
        b2 = (Button) b2.findViewById(R.id.b2);
        nom = (EditText) nom.findViewById(R.id.nom);
        mail = (EditText) mail.findViewById(R.id.email);
        phone = (EditText) phone.findViewById(R.id.phone);

        dbb = new DataBase(this);

        b1.setOnClickListener(v -> {
            if (!nom.getText().toString().equalsIgnoreCase("") &&
                    !mail.getText().toString().equalsIgnoreCase("") &&
                    !phone.getText().toString().equalsIgnoreCase("")) {

                boolean inserted = dbb.insertData(nom.getText().toString(),
                        mail.getText().toString(),
                        phone.getText().toString());

                if (inserted) {
                    Toast.makeText(MainActivity.this, "Insertion avec succès", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Echec d'insertion", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManagingDB.class);
                setContentView(intent);
            }
        });

    }

    public void setContentView(int contentView) {
        this.phone = contentView;
    }


    public class ManagingDB extends AppCompatActivity {
        private Button b;
        private ListView lv;
        private DataBase dataBase;

        @SuppressLint("WrongViewCast")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            b = findViewById(R.id.b1);
            lv = findViewById(editTextText);
            dataBase = new DataBase(this);

            getDataDir();

            public void viewData () {
                Cursor c = dataBase.getAllData();
                ArrayList<String> list = new ArrayList<>();
                if (c.getCount() == 0) {
                    Toast.makeText(ManagingDB.this, "La base est vide", Toast.LENGTH_SHORT).show();
                } else {
                    while (c.moveToNext()) {
                        list.add(c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3));
                        ListAdapter listAdapter = new ArrayAdapter<>(ManagingDB.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
                        lv.setAdapter(listAdapter);

                    }
                }
            }


            b.setOnClickListener(v -> {
                Intent int2 = new Intent(ManagingDB.this, MainActivity.class);
                startActivity(int2);
            });

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    String[] items = {"Modifier", "Supprimer"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ManagingDB.this);
                    builder.setTitle("Action");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                showUpdate(ManagingDB.this, lv, position);
                            } else if (which == 1) {
                                delete(lv, position);
                            }
                        }
                    });
                    builder.show();
                }
            });
        }

    }


    private void showUpdate(Activity ac, ListView lv, int p) {
        Dialog dialog = new Dialog(ac);
        dialog.setContentView(R.layout.update_db);
        dialog.setTitle("Update");
        final EditText name = (EditText) dialog.findViewById(R.id.editTextText2);
        final EditText mail = (EditText) dialog.findViewById(R.id.editTextText3);
        final EditText phone = (EditText) dialog.findViewById(R.id.editTextText4);
        Button bt =dialog.findViewById(R.id.b2);
        final String[] chaine =lv.getAdapter().getItem(p).toString().split(" ");
        name.setText(chaine[1]);
        mail.setText(chaine[2]);
        phone.setText(chaine[3]);
        int width = (int) (ac.getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = (int) (ac.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(chaine[0]);
                dbb.update(name.getText().toString(), mail.getText().toString(), phone.getText().toString(), i);
                Toast.makeText(ManagingDB.this, "Mise à jour avec succès", Toast.LENGTH_SHORT).show();
                int intent = new Intent(ManagingDB.this, ManagingDB.class);
                setContentView(intent);
                viewData();
            }
        });
    }

    private void delete (ListView lv, int p)
    {
        String[] chaine=lv.getAdapter().getItem(p).toString().split(" ");
        int i= Integer.parseInt(chaine[0]);
        dbb.delete(1);
        Toast.makeText(this, "Suppression avec succès", Toast.LENGTH_SHORT).show();
        viewData();
    }

    private void viewData() {
    }
}