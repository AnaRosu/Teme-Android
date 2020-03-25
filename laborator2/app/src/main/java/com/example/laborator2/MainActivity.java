package com.example.laborator2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

//ROSU ANA-MARIA, an 3, grupa B6

//lab4:
//1.implementat functionalitati meniu folosind intent explicit
//2.intent implicit am folosit la "Contact" unde folosesc Intent.ACTION_SEND
//3.formular de log in

//lab5:
//1.implementare settings activity ce foloseste shared preferences pentru schimbarea culorii backgroundului si textului si pentru schimbarea monezii de afisare a preturilor
//2.implementarea unei baze de date sqllite pe baza careia utilizatorul de poate loga (verificare username+parola in baza de date) + functionalitatea de sign in (cu validarile aferente)



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DatabaseHelper db;
    private SharedPreferences sharedPref;

    CustomAdapter customAdapter;

    int[] IMAGES = {R.drawable.item1, R.drawable.item2, R.drawable.item3, R.drawable.item4, R.drawable.item5};
    String[] NAMES = {"Rochie-Camasa", "Rochie neagra cu curea", "Rochie verde din voal", "Rochie verde plisata", "Rochie-camasa de blugi"};
    String[] PRICES = {"149", "139", "199", "149", "179"};
    String[] DESCRIPTIONS = {"Descriere item 1","Descriere item 2","Descriere item 3","Descriere item 4","Descriere item 5"};
    String USERNAME = "user";
    String PASSWORD = "pass";


    private TextView textView;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Log.d(TAG,"onCreate");
        setContentView(R.layout.activity_main);

        View rootView = getWindow().getDecorView().getRootView();
        //set backgorund colour according to shared preferences
        rootView.setBackgroundColor(Color.parseColor(sharedPref.getString("background_color", "#ffffff")));


        db = new DatabaseHelper(this);

        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textView_description);

        textView.setTextColor(Color.parseColor(sharedPref.getString("text_colour", "#000000")));

        customAdapter = new CustomAdapter();

        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(DESCRIPTIONS[position]);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.contact:
                //explicit intent
                Intent intent1 = new Intent(this, Contact.class);
                this.startActivity(intent1);
                return true;
            case R.id.home:
                Intent intent2 = new Intent(this,Home.class);
                this.startActivity(intent2);
                return true;
            case R.id.login:
                //apelez functia pt afisarea login-ului
                showDialog();
                return true;
            case R.id.signin:
                showDialogSignin();
                return true;
            case R.id.settings:
                Intent intent3 = new Intent(this, SettingsActivity.class);
                this.startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //functie pt login
    private void showDialog() {
        LayoutInflater linfl = LayoutInflater.from(this);
        View prompt = linfl.inflate(R.layout.dialog_login, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(prompt);
        final EditText user = (EditText) prompt.findViewById(R.id.username);
        final EditText pass = (EditText) prompt.findViewById(R.id.password);
        alertDialogBuilder.setTitle("Log in");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        String password = pass.getText().toString();
                        String username = user.getText().toString();
                        try
                        {
                            if ( username.length()<2 || password.length()<2)
                            {
                                Toast.makeText(MainActivity.this,"Invalid username or password", Toast.LENGTH_LONG).show();
                                showDialog();
                            }
                            else
                            {
                                boolean verify = db.verify(username,password);
                                if(verify)
                                    Toast.makeText(MainActivity.this,"Connection successful", Toast.LENGTH_LONG).show();
                                else {
                                    Toast.makeText(MainActivity.this,"Invalid username or password", Toast.LENGTH_LONG).show();
                                    showDialog();
                                }
                            }
                        }catch(Exception e)
                        {
                            Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();

            }
        });

        alertDialogBuilder.show();

    }

    //functie pentru sing in
    private void showDialogSignin(){
        LayoutInflater linfl = LayoutInflater.from(this);
        View prompt = linfl.inflate(R.layout.dialog_signin, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(prompt);
        final EditText user = (EditText) prompt.findViewById(R.id.username_signin);
        final EditText pass1 = (EditText) prompt.findViewById(R.id.password_signin);
        final EditText pass2 = (EditText) prompt.findViewById(R.id.password_signin_repeat);
        alertDialogBuilder.setTitle("Sign in");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        String password = pass1.getText().toString();
                        String password_repeat = pass2.getText().toString();
                        String username = user.getText().toString();
                        try
                        {
                            if ( username.length()<2 || password.length()<2 || password_repeat.length()<2)
                            {
                                Toast.makeText(MainActivity.this,"Invalid username or password", Toast.LENGTH_LONG).show();
                                showDialogSignin();
                            }
                            else {
                                if (!password.equals(password_repeat)) {
                                    Toast.makeText(MainActivity.this, "The passwords are different", Toast.LENGTH_LONG).show();
                                    showDialogSignin();
                                } else {
                                    boolean checkusern = db.username_exists(username);
                                    if(checkusern){
                                        boolean insert = db.insert(username,password);
                                        if (insert)
                                            Toast.makeText(MainActivity.this, "Registration successful!", Toast.LENGTH_LONG).show();
                                        else
                                            Toast.makeText(MainActivity.this, "Could not insert in the database!", Toast.LENGTH_LONG).show();
                                            showDialogSignin();
                                    }
                                    else
                                        Toast.makeText(MainActivity.this, "User already exists in the database!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }catch(Exception e)
                        {
                            Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();

            }
        });

        alertDialogBuilder.show();

    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        final TextView text = (TextView) findViewById(R.id.textView_description);
        CharSequence userText = text.getText();
        outState.putCharSequence("savedtext", userText);
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //savedUser = savedInstanceState.getString("TEXT");
        //Log.d("enregistred value", savedUser);
        final TextView text = (TextView) findViewById(R.id.textView_description);
        CharSequence userText= savedInstanceState.getCharSequence("savedtext");
        text.setText(userText);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        View rootView = getWindow().getDecorView().getRootView();
        //set backgorund and text colour according to shared preferences
        rootView.setBackgroundColor(Color.parseColor(sharedPref.getString("background_color", "#ffffff")));
        textView.setTextColor(Color.parseColor(sharedPref.getString("text_colour", "#000000")));
        customAdapter.notifyDataSetChanged();
        /*textView_name.setTextColor(Color.parseColor(sharedPref.getString("text_colour", "#000000")));
        textView_price.setTextColor(Color.parseColor(sharedPref.getString("text_colour", "#000000")));*/
        Log.d(TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    class CustomAdapter extends BaseAdapter{
        private TextView textView_name;
        private TextView textView_price;


        @Override
        public int getCount() {
            return IMAGES.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.customlayout, null);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
            textView_name = (TextView)convertView.findViewById(R.id.textView_name);
            textView_price = (TextView)convertView.findViewById(R.id.textView_prices);

            //set text colour according to the preferences
            textView_name.setTextColor(Color.parseColor(sharedPref.getString("text_colour", "#000000")));
            textView_price.setTextColor(Color.parseColor(sharedPref.getString("text_colour", "#000000")));

            imageView.setImageResource(IMAGES[position]);
            textView_name.setText(NAMES[position]);
            String price;
            if(sharedPref.getString("currency", "RON").equals("EURO")){
                price = ((int)Math.round(Integer.parseInt(PRICES[position])/4.7)) + " " + sharedPref.getString("currency", "RON");
            }
            else {
                price = PRICES[position] + " " + sharedPref.getString("currency", "RON");
            }
            textView_price.setText(price);
            return convertView;
        }
    }
}
