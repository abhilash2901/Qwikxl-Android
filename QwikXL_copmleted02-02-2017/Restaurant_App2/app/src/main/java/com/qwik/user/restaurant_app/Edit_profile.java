package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Edit_profile extends Activity {
    Animation shake = null;
    static SharedPreferences pref = null;
    String getStatus = null, oldpassword = null, encodedImagedoc = null;
    ProgressDialog pgBar = null, pgBars = null, pgBar1 = null, pgBar2 = null;
    ImageView profile_pic = null;
    EditText f_name = null, l_name = null, email = null, mobile = null, address = null;
    Button update_profile = null;
    Spinner sp_country = null;
    String country_id = null, country_code = null, image = null, country_name;
    ImageButton back = null;
    GetSet_country getset_country = null;
    ListViewAdapter1 listAdapter1 = null;
    private static final int REQUEST_CAMERA = 1880;
    private static final int SELECT_FILE = 1990;
    ImageLoader imageLoader = null;
    DisplayImageOptions options = null;
    JSONObject Parent = new JSONObject();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        f_name = (EditText) findViewById(R.id.f_name);
        l_name = (EditText) findViewById(R.id.l_name);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobile);
        address = (EditText) findViewById(R.id.address);
        back = (ImageButton) findViewById(R.id.back);
        update_profile = (Button) findViewById(R.id.update);
        sp_country = (Spinner) findViewById(R.id.sp_country);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        try {
            getStatus = pref.getString("username", "");

            getting_profiledata(getStatus);
            getting_country_code();

            update_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (f_name.getText().toString().equalsIgnoreCase("")) {
                            f_name.setError("Enter first name");
                            f_name.startAnimation(shake);
                        } else if (l_name.getText().toString().equalsIgnoreCase("")) {
                            l_name.setError("Enter last name");
                            l_name.startAnimation(shake);
                        } else if (mobile.getText().toString().equalsIgnoreCase("")) {
                            mobile.setError("Enter mobile no");
                            mobile.startAnimation(shake);
                        } else {
                            Parent.put("firstname", f_name.getText().toString());
                            Parent.put("lastname", l_name.getText().toString());
                            Parent.put("email", getStatus);
                            Parent.put("country_code", country_code);
                            Parent.put("mobile", mobile.getText().toString());
                            Parent.put("country", country_name);
                            Parent.put("address", address.getText().toString());
                            Log.e("update_parent", Parent.toString());

                            updating_profile(Parent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), My_account.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    //     onBackPressed();
                }
            });
            sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    country_code = getset_country.getcountrylist().get(position).getcountry_code();
                    country_name = getset_country.getcountrylist().get(position).getCountry_name();
                    Log.e("country_code", country_code + ", " + country_name);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();
                }
            });
        } catch (Exception e) {

        }
    }

    private void updating_profile(JSONObject parent) {
        try {
            pgBar = Progress_class.createProgressDialog(Edit_profile.this);
            pgBar.show();

            ApiClient.getPosApp().getupdate(parent.toString(), new Callback<GetSet_login>() {
                @Override
                public void success(GetSet_login getSet_login, Response response) {
                    System.out.println("Sucess" + response.getUrl());
                    if (pgBar.isShowing()) {
                        pgBar.dismiss();
                    }
                    String res = getSet_login.getstatus();

                    if (res.equalsIgnoreCase("Updation Successful")) {
                        try {
                            pref = getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("mobile", getSet_login.getmobile());
                            editor.commit();
                            Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(getApplicationContext(), Home.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (res.equalsIgnoreCase("Updation Failed")) {

                        Toast.makeText(getApplicationContext(), "Updation Failed, Try Again", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {

                }
            });
        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }

    private void getting_profiledata(String getStatus) {
        pgBars = Progress_class.createProgressDialog(Edit_profile.this);
        pgBars.show();
        try {
            ApiClient.getPosApp().getmyaccount(getStatus, new Callback<GetSet_myaccount2>() {
                @Override
                public void success(GetSet_myaccount2 getSet_myaccount, Response response) {

                    System.out.println("sucess Login  " + response);
                    if (pgBars.isShowing()) {
                        pgBars.dismiss();
                    }
                    country_id = getSet_myaccount.getcountrycodeId();
                    oldpassword = getSet_myaccount.getaccount().get(0).getpassword();
                    f_name.setText(getSet_myaccount.getaccount().get(0).getFirstnamename());
                    l_name.setText(getSet_myaccount.getaccount().get(0).getlastname());
                    email.setText(getSet_myaccount.getaccount().get(0).getemail());
                    mobile.setText(getSet_myaccount.getaccount().get(0).getmobile());
                    address.setText(getSet_myaccount.getaccount().get(0).getaddress());
                    image = ApiClient.url_common + "/" + getSet_myaccount.getaccount().get(0).getimage();
                    // Toast.makeText(getApplicationContext(),country_id,Toast.LENGTH_LONG).show();
                    if (getSet_myaccount.getaccount().get(0).getimage().equals("")) {
                        profile_pic.setBackgroundResource(R.drawable.account_pic);
                    } else {
                        seting_image();
                    }

                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    if (retrofitError.getResponse() != null) {
                        Log.e("Retrofit error", retrofitError.getCause().toString());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getting_country_code() {
        pgBar1 = Progress_class.createProgressDialog(Edit_profile.this);
        pgBar1.show();
        ApiClient.getPosApp().getcountry(new Callback<GetSet_country>() {
            public void success(GetSet_country cartModel, Response response) {
                Log.e("arrauy", response.toString());
                System.out.print("*******Sucess download data********" + response.getUrl());
                if (pgBar1.isShowing()) {
                    pgBar1.dismiss();
                }
                consumeApiData1(cartModel);
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.print("*******Sucess download data********");
                consumeApiData1(null);
            }

            private void consumeApiData1(final GetSet_country getSet_country) {
                getset_country = getSet_country;
                if (getset_country != null) {
                    try {
                        listAdapter1 = new ListViewAdapter1(getApplicationContext(), getSet_country);
                        sp_country.setAdapter(listAdapter1);
                        sp_country.setSelection(Integer.parseInt(country_id) - 1);
                        sp_country.setFocusableInTouchMode(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Data Detected", Toast.LENGTH_SHORT).show();
                    if (pgBar.isShowing()) {
                        pgBar.dismiss();
                    }
                }
            }
        });
    }

    public class ListViewAdapter1 extends BaseAdapter {

        Context this_context;
        LayoutInflater inflater;
        ImageLoader imageLoader;
        DisplayImageOptions options;
        ImageView im;

        public ListViewAdapter1(Context context, GetSet_country getSet_count) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getset_country = getSet_count;
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration
                    .createDefault(getApplicationContext()));
            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .showImageOnLoading(R.drawable.ic_launcher).cacheOnDisk(true)
                    .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return getset_country.getcountrylist().size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = inflater.inflate(R.layout.list_row_country, parent, false);
            TextView cat_name = (TextView) itemView.findViewById(R.id.cat_name);
            TextView country_name = (TextView) itemView.findViewById(R.id.country_name);
            im = (ImageView) itemView.findViewById(R.id.im);
            try {
                imageLoader.displayImage(
                        getset_country.getcountrylist().get(position).getflag(), im, options,
                        new SimpleImageLoadingListener() {
                            Animation rotation;

                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view,
                                                          Bitmap loadedImage) {
                                im.setScaleType(ImageView.ScaleType.FIT_XY);
                            }
                        }, new ImageLoadingProgressListener() {

                            public void onProgressUpdate(String imageUri, View view,
                                                         int current, int total) {
                            }
                        });
                cat_name.setText(getset_country.getcountrylist().get(position).getcountry_code());
                country_name.setText(getset_country.getcountrylist().get(position).getCountry_name());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return itemView;
        }
    }

    private void selectImage() {
        try {
            final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(Edit_profile.this);
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Take Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    } else if (items[item].equals("Choose from Library")) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(
                                Intent.createChooser(intent, "Select File"),
                                SELECT_FILE);
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 99, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                profile_pic.setImageBitmap(getRoundedShape(thumbnail));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                encodedImagedoc = Base64.encodeToString(b, Base64.DEFAULT);

                uploading_picture(getStatus, encodedImagedoc);

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                        null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap photo;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                photo = BitmapFactory.decodeFile(selectedImagePath, options);
                //  profile_pic.setImageBitmap(bm);
                profile_pic.setImageBitmap(getRoundedShape(photo));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                encodedImagedoc = Base64.encodeToString(b, Base64.DEFAULT);

                uploading_picture(getStatus, encodedImagedoc);

            }
        }
    }
    private void uploading_picture(String getStatus, String encodedImagedoc) {
        pgBar2 = Progress_class.createProgressDialog(Edit_profile.this);
        pgBar2.show();
        try {
            ApiClient.getPosApp().getprofile_pic(getStatus, encodedImagedoc, new Callback<GetSet_login>() {
                @Override
                public void success(GetSet_login getSet_login, Response response) {

                    if (pgBar2.isShowing()) {
                        pgBar2.dismiss();
                    }
                    String res1 = getSet_login.getstatus();
                    if (res1.equalsIgnoreCase("Profile Picture Uploaded")) {
                        try {
                            Toast.makeText(getApplicationContext(), res1 + "", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (pgBar2.isShowing()) {
                            pgBar2.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), res1 + "", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    if (retrofitError.getResponse() != null) {
                        Log.e("Retrofit error", retrofitError.getCause().toString());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {

        Bitmap bitmap = scaleBitmapImage;
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        return circleBitmap;
    }

    private void seting_image() {
        try {
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration
                    .createDefault(getApplicationContext()));
            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .showImageOnLoading(R.drawable.ic_launcher).cacheOnDisk(true)
                    .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            imageLoader.displayImage(
                    image, profile_pic, options,
                    new SimpleImageLoadingListener() {
                        Animation rotation;

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {

                            profile_pic.setImageBitmap(getRoundedShape(loadedImage));
                        }
                    }, new ImageLoadingProgressListener() {

                        public void onProgressUpdate(String imageUri, View view,
                                                     int current, int total) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), My_account.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}


