package com.example.toshibat.practicalapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.ImageWriter;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
   Methods methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        methods=new Methods(this);

        final TextView id = (TextView) findViewById(R.id.editText2_id);
        final TextView name = (TextView) findViewById(R.id.editText3_cmpnyname);
        final TextView addr = (TextView) findViewById(R.id.editText_cmpnyaddr);
        final TextView phn1=(TextView)findViewById(R.id.editText_phn1);
        final TextView phn2=(TextView)findViewById(R.id.phn2);
        final TextView fax=(TextView)findViewById(R.id.editText3_fax);
        final TextView email=(TextView)findViewById(R.id.editText4_email);
        final TextView web=(TextView)findViewById(R.id.editText5_web);



        final ImageView iv= (ImageView)findViewById(R.id.imageView);





        Button clr = (Button) findViewById(R.id.btnclr);
        Button save = (Button) findViewById(R.id.savebtn);
        final Button view = (Button)findViewById(R.id.viewdatabtn);

        iv.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,"Long Press",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        registerForContextMenu(iv);

        clr.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        name.setText("");
                        id.setText("");
                        addr.setText("");
                        phn1.setText("");
                        phn2.setText("");
                        fax.setText("");
                        email.setText("");
                        web.setText("");
                        iv.setImageResource(R.mipmap.ic_launcher);
                        Toast.makeText(MainActivity.this, "Data Cleared", Toast.LENGTH_SHORT).show();
                       // showMessage("data","cleared");

                    }
                }
        );
        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bitmap photo = ((BitmapDrawable)iv.getDrawable()).getBitmap();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        byte[] bArray = bos.toByteArray();

                        String idt = id.getText().toString();
                        Integer idt1=new Integer(idt).intValue();

                      boolean inserted= methods.insertData(idt1, name.getText().toString(), addr.getText().toString(), phn1.getText().toString(), phn2.getText().toString(),
                               fax.getText().toString(), email.getText().toString(), web.getText().toString(),bArray);

                        if(inserted==true) {
                            Toast.makeText(MainActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
                            //showMessage("Data", "Saved",null);
                        }
                        else
                           Toast.makeText(MainActivity.this, "Data failed", Toast.LENGTH_SHORT).show();

                    }
                }
        );

        view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor cursor= methods.viewData();

                        if(cursor.getCount()==0){
                            Toast.makeText(MainActivity.this,"Error Retreving data",Toast.LENGTH_SHORT).show();
                            showMessage("Error", "Nothing found",null);
                            return;
                        }
                        StringBuffer buffer=new StringBuffer();
                        while (cursor.moveToNext()) {
                            buffer.append("ID : " + cursor.getString(0) + "\n");
                            buffer.append("COMPANY NAME : " + cursor.getString(1) + "\n");
                            buffer.append("COMPANY ADDRESS : " + cursor.getString(2) + "\n");
                            buffer.append("COMPANY PHONE 1 : " + cursor.getString(3) + "\n");
                            buffer.append("COMPANY PHONE 2 : " + cursor.getString(4) + "\n");
                            buffer.append("COMPANY FAX : " + cursor.getString(5) + "\n");
                            buffer.append("COMPANY EMAIL : " + cursor.getString(6) + "\n");
                            buffer.append("COMPANY WEB : " + cursor.getString(7) + "\n");

                            byte[] imgByte = cursor.getBlob(8);
                            //cursor.close();

                            Bitmap i = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);

                            View v2 = new ImageView(getBaseContext());
                            ImageView image;
                            image = new ImageView(v.getContext());
                            image.setImageBitmap(i);

                            buffer.append("COMPANY LOGO : " + "" + "\n\n");


                            Toast.makeText(MainActivity.this, "View Pressed", Toast.LENGTH_SHORT).show();

                            showMessage("DATA ", buffer.toString(),image);

                        }
                    }
                }
        );




    }
    public void showMessage(String title,String Message,ImageView a){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
       // ImageView iv2= (ImageView)findViewById(R.id.imageView2);



        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.setView(a);
        builder.show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 1234:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();


                     Bitmap selectedimg = BitmapFactory.decodeFile(filePath);
                    ImageView imageView= (ImageView) findViewById(R.id.imageView);

                    ByteArrayOutputStream b=new ByteArrayOutputStream();
                    selectedimg.compress(Bitmap.CompressFormat.JPEG,100,b);
                    imageView.setImageBitmap(selectedimg);


                }
        }

    };



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
