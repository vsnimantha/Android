package com.example.toshibat.mysqlapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    Button add;
    EditText id,name, address,phone1,phone2,fax,email,web;
    ImageView iv;
    Connection connect;
    Context context;
    PreparedStatement preparedStatement;
    Statement st;
    String ipaddress, db, username, password;

    Methods methods;


    @SuppressLint("NewApi")
    private Connection ConnectionHelper(String user, String password,
                                        String database, String server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server + ";"
                    + "databaseName=" + database + ";user=" + user
                    + ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return connection;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = (Button) findViewById(R.id.btnadd);
        iv=(ImageView)findViewById(R.id.imageView);
        id=(EditText)findViewById(R.id.txtid);
        name = (EditText) findViewById(R.id.txtname);
        address = (EditText) findViewById(R.id.txtaddrs);
        phone1=(EditText) findViewById(R.id.txtphn1);
        phone2=(EditText) findViewById(R.id.txtphn2);
        fax=(EditText) findViewById(R.id.txtfax);
        email=(EditText) findViewById(R.id.txtemail);
        web=(EditText) findViewById(R.id.txtweb);
        methods=new Methods(this);

        ipaddress = "192.168.1.4";   // this is the ip address of your computer  where sql is installed
        db = "company"; // name of your database
        username = "vs"; // username for the database
        password = "123"; // password
        connect = ConnectionHelper(username, password, db, ipaddress);

        iv.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,"Please Long Press to Add an Image ",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        registerForContextMenu(iv);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {




                        String a, b, c, d, e, f, g, h;
                        Cursor cursor= methods.viewData();
                        a = id.getText().toString();
                        b = name.getText().toString();
                        c = address.getText().toString();
                        d = phone1.getText().toString();
                        e = phone2.getText().toString();
                        f = fax.getText().toString();
                        g = email.getText().toString();
                        h = web.getText().toString();

                        //image to binary
                         Bitmap photo = ((BitmapDrawable) iv.getDrawable()).getBitmap();
                         ByteArrayOutputStream bos = new ByteArrayOutputStream();
                         photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
                         byte[] bArray = bos.toByteArray();



                            Toast.makeText(MainActivity.this,"Connecting",Toast.LENGTH_SHORT).show();

                         if(isOnline()&&connect!=null) {
                             Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_SHORT).show();

                                if(cursor.getCount()!=0) {
                                 Toast.makeText(MainActivity.this, "Offline database data found", Toast.LENGTH_SHORT).show();


                                 while (cursor.moveToNext()) {
                                     Toast.makeText(MainActivity.this, "Copying data from offline database", Toast.LENGTH_SHORT).show();
                                     String temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8;
                                     byte[] tempimg = null;

                                     temp1 = cursor.getString(0);
                                     temp2 = cursor.getString(1);
                                     temp3 = cursor.getString(2);
                                     temp4 = cursor.getString(3);
                                     temp5 = cursor.getString(4);
                                     temp6 = cursor.getString(5);
                                     temp7 = cursor.getString(6);
                                     temp8 = cursor.getString(7);
                                     tempimg = cursor.getBlob(8);

                                   /*  Toast.makeText(MainActivity.this,"Copying ID: " + temp1, Toast.LENGTH_SHORT).show();
                                     Toast.makeText(MainActivity.this,"Copying Name: "+temp2, Toast.LENGTH_SHORT).show();
                                     Toast.makeText(MainActivity.this,"Copying Address: "+ temp3, Toast.LENGTH_SHORT).show();
                                     Toast.makeText(MainActivity.this,"Copying Phone 1: "+temp4, Toast.LENGTH_SHORT).show();
                                     Toast.makeText(MainActivity.this,"Copying Phone 2: "+ temp5, Toast.LENGTH_SHORT).show();
                                     Toast.makeText(MainActivity.this,"Copying Fax: "+ temp6, Toast.LENGTH_SHORT).show();
                                     Toast.makeText(MainActivity.this,"Copying Email: "+ temp7, Toast.LENGTH_SHORT).show();
                                     Toast.makeText(MainActivity.this,"Copying Web: "+ temp8, Toast.LENGTH_SHORT).show();
                                     Toast.makeText(MainActivity.this,"Copying Image Data: "+ tempimg.toString(), Toast.LENGTH_SHORT).show();*/





                                     st = connect.createStatement();
                                     preparedStatement=connect.prepareStatement("insert into COMPANY values ('" + temp1 + "','" + temp2 + "','" + temp3 + "','" + temp4 + "','" + temp5 + "','" + temp6 + "','" + temp7 + "','" + temp8 + "','" + tempimg + "')");
                                     preparedStatement.executeUpdate();

                                     Toast.makeText(MainActivity.this, "OFFLINE DATA SAVED", Toast.LENGTH_SHORT).show();
                                     methods.deleteAll();
                                     Toast.makeText(MainActivity.this, "OFFLINE DATABASE CLEARED", Toast.LENGTH_SHORT).show();


                                 }
                             }
                             else{
                                 Toast.makeText(MainActivity.this,"No offline data found", Toast.LENGTH_SHORT).show();
                             }



                             //else {


                                 st = connect.createStatement();
                                 preparedStatement = connect.prepareStatement("insert into COMPANY values ('" + a + "','" + b + "','" + c + "','" + d + "','" + e + "','" + f + "','" + g + "','" + h + "','" + bArray + "')");


                                 preparedStatement.executeUpdate();


                                 Toast.makeText(MainActivity.this, "DATA SAVED", Toast.LENGTH_SHORT).show();
                            // }
                       /* id.setText("ENTER COMPANY ID");
                        name.setText("ENTER COMPANY NAME");
                        address.setText("ENTER COMPANY ADDRESS");
                        phone1.setText("ENTER COMPANY PHONE 1");
                        phone2.setText("ENTER COMPANY PHONE 2");
                        fax.setText("ENTER COMPANY FAX");
                        email.setText("ENTER COMPANY EMAIL");
                        web.setText("ENTER COMPANY WEB ");
                        // iv.setImageBitmap();*/


                   }else{
                             Toast.makeText(MainActivity.this,"Connection Failed",Toast.LENGTH_SHORT).show();
                             Toast.makeText(MainActivity.this,"Updating to Local database",Toast.LENGTH_SHORT).show();

                             int a1=Integer.parseInt(a);

                             boolean isInserted=methods.insertData(a1,b,c,d,e,f,g,h,bArray);

                             if(isInserted==true){
                                 Toast.makeText(MainActivity.this,"Updating to Local database Success",Toast.LENGTH_SHORT).show();
                             }
                             else
                             {
                                 Toast.makeText(MainActivity.this,"Updating to Local database Failed",Toast.LENGTH_SHORT).show();
                             }



                      /*  StringBuffer buffer = new StringBuffer();
                        buffer.append(a+"\n");
                        buffer.append(b+"\n");
                        buffer.append(c+"\n");
                        buffer.append(d+"\n");
                        buffer.append(e+"\n");
                        buffer.append(f+"\n");
                        buffer.append(g+"\n");
                        buffer.append(h+"\n");
                        buffer.append(bArray+"\n\n");*/

                      /*  String [] data =null;
                        data[0]= String.valueOf(buffer.charAt(0));
                        data[1]= String.valueOf(buffer.charAt(1));
                        data[2]= String.valueOf(buffer.charAt(2));
                        data[3]= String.valueOf(buffer.charAt(3));
                        data[4]= String.valueOf(buffer.charAt(4));
                        data[5]= String.valueOf(buffer.charAt(5));
                        data[6]= String.valueOf(buffer.charAt(6));
                        data[7]= String.valueOf(buffer.charAt(7));
                        data[8]= String.valueOf(buffer.charAt(8));*/



/*
                        File myFile = new File("/sdcard/mysdfile.txt");
                        myFile.createNewFile();
                        FileOutputStream fOut = new FileOutputStream(myFile);
                        OutputStreamWriter myOutWriter =
                                new OutputStreamWriter(fOut);
                        myOutWriter.append(String.valueOf(buffer.charAt(0)));
                        myOutWriter.append(String.valueOf(buffer.charAt(1)));
                        myOutWriter.append(String.valueOf(buffer.charAt(2)));
                        myOutWriter.append(String.valueOf(buffer.charAt(3)));
                        myOutWriter.append(String.valueOf(buffer.charAt(4)));
                        myOutWriter.append(String.valueOf(buffer.charAt(5)));
                        myOutWriter.append(String.valueOf(buffer.charAt(6)));
                        myOutWriter.append(String.valueOf(buffer.charAt(7)));
                        myOutWriter.append(String.valueOf(buffer.charAt(8)));

                        myOutWriter.close();
                        fOut.close();
                        Toast.makeText(getBaseContext(),
                                "Done writing SD 'mysdfile.txt'",
                                Toast.LENGTH_SHORT).show();*/

                    }

                } catch (SQLException e) {
                    Toast.makeText(MainActivity.this,"DATA SAVE FAILED ERROR "+e.getMessage().toString(),Toast.LENGTH_SHORT).show();



                }

            }
        });
    }
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Add Image");
        menu.add(0, v.getId(), 0, "Select From Gallery");
        menu.add(0, v.getId(), 0, "Open Camera");

    }
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle() == "Select From Gallery") {
            Toast.makeText(this, "Select From Gallery invoked", Toast.LENGTH_SHORT).show();
           /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "content://media/internal/images/media"));
                     startActivity(intent);*/
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    final int ACTIVITY_SELECT_IMAGE = 1234;
                    startActivityForResult(i, ACTIVITY_SELECT_IMAGE);


        } else if (item.getTitle() == "Open Camera") {
            Toast.makeText(this, "Open Camera invoked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(intent);

        } else {
            return false;
        }
        return true;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1234:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();


                    Bitmap selectedimg = BitmapFactory.decodeFile(filePath);
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);

                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    selectedimg.compress(Bitmap.CompressFormat.JPEG, 100, b);
                    imageView.setImageBitmap(selectedimg);


                }
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
