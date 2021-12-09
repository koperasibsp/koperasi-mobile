package id.co.bspguard.android.bravo.changeavatar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.acra.config.DefaultRetryPolicy;
import org.acra.config.RetryPolicy;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import id.co.bspguard.android.bravo.AlertDialog.KAlertDialog;
import id.co.bspguard.android.bravo.MainActivity;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.calculator.Calculator;
import id.co.bspguard.android.bravo.contact.Contact;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.home.ActivityAwal;

public class ChangeAvatar extends AppCompatActivity implements View.OnClickListener {
  public static final String KEY_User_Document1 = "doc1";
  ImageView IDProf;
  Button Upload_Btn;
  VolleyObjectResult vor = null;
  VolleyObjectService vos;

  private String Document_img1="";
  private static final int MY_CAMERA_PERMISSION_CODE = 100;
  private static final int CAMERA_REQUEST = 1888;
  Fungsi fungsi = new Fungsi();
  String url = fungsi.url()+"change-avatar";

  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_change_avatar);


    IDProf=(ImageView)findViewById(R.id.IdProf);
    Upload_Btn=(Button)findViewById(R.id.UploadBtn);

    IDProf.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
          requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
          selectImage();
        }


      }
    });

    Upload_Btn.setOnClickListener(this);

  }

  @Override
  public void onClick(View v) {
    if (Document_img1.equals("") || Document_img1.equals(null)) {
      ContextThemeWrapper ctw = new ContextThemeWrapper(ChangeAvatar.this, R.style.AppTheme);
      final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
      alertDialogBuilder.setTitle("Id Prof Can't Empty ");
      alertDialogBuilder.setMessage("Id Prof Can't empty please select any one document");
      alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {

        }
      });
      alertDialogBuilder.show();
      return;
    }
    else{
      ACProgressFlower dialog = new ACProgressFlower.Builder(ChangeAvatar.this)
        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
        .themeColor(Color.WHITE)
        .text("Loading ...")
        .fadeColor(Color.DKGRAY).build();
      dialog.show();

//    Map<String,String> map = new HashMap<>();
//    map.put(KEY_User_Document1,Document_img1);
      final String access_token = fungsi.getdatalogin(ChangeAvatar.this);

      HashMap<String, String> dt = new HashMap<String, String>();
      dt.put("Content-Type", "application/json; charset=UTF-8");
      dt.put("change_image", Document_img1);
      dt.put("token", access_token);
      final JSONObject data = new JSONObject(dt);

      vor = new VolleyObjectResult() {
        @Override
        public void resSuccess(String requestType, JSONObject response) {
          try {

            boolean error = response.getBoolean("error");

            if(error){

              new KAlertDialog(ChangeAvatar.this, KAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(response.getString("message"))
                .setConfirmText("OK")
                .show();
              return;
            }


            Intent goHome = new Intent(ChangeAvatar.this, MainActivity.class);
            startActivity(goHome);


          } catch (Exception e) {
            e.printStackTrace();

          }
        }
        @Override
        public void resError(String requestType, VolleyError error) {
          View view = findViewById(R.id.coordinator);
          String message = "Maaf terjadi kesalahan.";
          fungsi.showSnackbar(view, message);
        }
      };
      vos = new VolleyObjectService(vor, ChangeAvatar.this);
      vos.postJsonObject("POSTCALL", url, data);
      dialog.dismiss();
    }
  }

  private void selectImage() {
    final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
    AlertDialog.Builder builder = new AlertDialog.Builder(ChangeAvatar.this);
    builder.setTitle("Upload Avatar");
    builder.setItems(options, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int item) {
        if (options[item].equals("Take Photo"))
        {
//          Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//          startActivityForResult(cameraIntent, 1);

          File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
          Log.d("Path", String.valueOf(mediaStorageDir));
          File mediaFile;
          mediaFile = new File(mediaStorageDir, "temp_bsp.jpg");
          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(ChangeAvatar.this, getPackageName()+".provider", mediaFile));
          startActivityForResult(intent, 1);

        }
        else if (options[item].equals("Choose from Gallery"))
        {
          Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          startActivityForResult(intent, 2);
        }
        else if (options[item].equals("Cancel")) {
          dialogInterface.dismiss();
        }
      }
    });
    builder.show();

  }

  public String BitMapToString(Bitmap userImage1) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
    byte[] b = baos.toByteArray();
    Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
    return Document_img1;
  }

  public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
    int width = image.getWidth();
    int height = image.getHeight();

    float bitmapRatio = (float)width / (float) height;
    if (bitmapRatio > 1) {
      width = maxSize;
      height = (int) (width / bitmapRatio);
    } else {
      height = maxSize;
      width = (int) (height * bitmapRatio);
    }
    return Bitmap.createScaledBitmap(image, width, height, true);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 1) {
        String root = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        File f = new File(root, "temp_bsp.jpg");

        try {
          Bitmap bitmap;
          BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
          bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
          bitmap = getResizedBitmap(bitmap, 400);
          IDProf.setImageBitmap(bitmap);
          BitMapToString(bitmap);
          String path = android.os.Environment
            .getExternalStorageDirectory()
            + File.separator
            + "bsp" + File.separator + "default";
          OutputStream outFile = null;
          File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
          try {
            outFile = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
            outFile.flush();
            outFile.close();
          } catch (FileNotFoundException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          } catch (Exception e) {
            e.printStackTrace();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else if (requestCode == 2) {
        Uri selectedImage = data.getData();
        String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        String picturePath = c.getString(columnIndex);
        c.close();
        Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
        thumbnail=getResizedBitmap(thumbnail, 400);
        Log.w("path", picturePath+"");
        IDProf.setImageBitmap(thumbnail);
        BitMapToString(thumbnail);
      }
    }

  }

}
