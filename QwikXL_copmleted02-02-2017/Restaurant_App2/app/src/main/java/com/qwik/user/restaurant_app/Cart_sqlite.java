package com.qwik.user.restaurant_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Cart_sqlite extends Activity {

    TextView idText = null, total = null;
    TextView textthree = null, textTwo = null, textfour = null;
    Button close = null, clear = null, reward_apply = null, order = null, discount_apply = null;
    EditText quant = null, reward = null;
    int idd = 0;
    static int flag = 0;
    String id = null, reward_get = null, getStatus = null, reward_pointt = null;
    Double sum = 0.0;
    TableLayout dataTable = null;
    TableRow tableRow = null;
    AABDatabaseManager db = null;
    ArrayList<ArrayList<Object>> data = null;
    ArrayList<Object> row = null;
    static SharedPreferences pref = null;
    ArrayList<Integer> id_list = new ArrayList<>();
    SQLiteDatabase add = null;
    AlertDialog.Builder ab = null;
    Data_values.Tools toolid = new Data_values.Tools();
    Bundle b = null;
    ProgressDialog pgBar = null;
    static Double reward_value = 500.0;
    Double GrandTotal = 0.0, sum_total;
    Animation shake = null;
    int checkout_flag=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_sqlite);
        dataTable = (TableLayout) findViewById(R.id.datatable);
        total = (TextView) findViewById(R.id.total);
        order = (Button) findViewById(R.id.order);
        discount_apply = (Button) findViewById(R.id.discount_apply);
        reward_apply = (Button) findViewById(R.id.reward_apply);
        reward = (EditText) findViewById(R.id.reward);
        db = new AABDatabaseManager(this);
        pgBar = new ProgressDialog(Cart_sqlite.this);
        b = new Bundle();
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        pref = getSharedPreferences("Login", MODE_PRIVATE);
        getStatus = pref.getString("username", "");
        data = db.getAllRowsAsArrays();
        try {
            if (getStatus.equals("")) {
                Intent i = new Intent(getApplicationContext(), Home_login.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            } else {
                updateTable();
                Total_amount();
            }

            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (pref.getString("username", "").equals("")) {
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    } else if (data.size() <= 0) {
                        Toast.makeText(getApplication(), "Cart is Empty", Toast.LENGTH_SHORT).show();
                    } else

                    {
                        b.putString("Grandtotal", String.valueOf(GrandTotal));
                        b.putString("rewardpoint", String.valueOf(reward_get));
                        Intent i = new Intent(getApplicationContext(), Payment_option.class);
                        i.putExtras(b);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                    }

                }


            });
            reward_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (reward.getText().toString().equals("")) {
                        reward.setError("Enter Reward Point");
                        reward.startAnimation(shake);
                        // Toast.makeText(getApplication(), "Enter Reward Point", Toast.LENGTH_SHORT).show();
                    } else if ((Integer.parseInt(reward.getText().toString()) < 500) || (Integer.parseInt(reward.getText().toString()) > 1000)) {
                        Toast.makeText(getApplication(), "Enter Valid Reward Point", Toast.LENGTH_SHORT).show();
                    } else {
                        getting_reward(getStatus);
                    }
                }
            });
            discount_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplication(), "Under Process", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getting_reward(String getStatus) {
        {
            pgBar = Progress_class.createProgressDialog(Cart_sqlite.this);
            pgBar.show();
            try {
                ApiClient.getPosApp().getrewardpoint(getStatus, new Callback<GetSet_login>() {
                    @Override
                    public void success(GetSet_login getSet_login, Response response) {
                        System.out.println("sucess Login  " + response);
                        if (pgBar.isShowing()) {
                            pgBar.dismiss();
                        }
                        String res = getSet_login.getstatus();
                        if (res.equalsIgnoreCase("true")) {
                            try {
                                reward_get = reward.getText().toString();
                                reward_pointt = getSet_login.getreward();
                                reward.setText("");
                                if (Double.parseDouble(reward_get) <= Double.parseDouble(reward_pointt)) {
                                    Calculating_Grandtotal(reward_get);
                                } else if (!reward_pointt.equals(reward_get)) {
                                    reward_get = "";
                                    Toast.makeText(getApplicationContext(), "Your Reward point is Invalid.Valid Reward point is" + reward_pointt, Toast.LENGTH_LONG).show();
                                } else {
                                    reward_get = "";
                                    Toast.makeText(getApplicationContext(), "Your Reward point is Invalid", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {

                            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        if (pgBar.isShowing()) {
                            pgBar.dismiss();
                        }
                        if (retrofitError.getResponse() != null) {
                            Log.e("Retrofit error", retrofitError.getCause().toString());
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void Calculating_Grandtotal(String reward_pointt) {
        try {
            Double point = Double.parseDouble(reward_pointt) / reward_value;
            Double grand_total = (sum * point) / 100;
            GrandTotal = sum - grand_total;
            //  total.setText(String.valueOf(GrandTotal));
            total.setText(String.format("%.2f", GrandTotal));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void Total_amount() {
        sum = 0.0;
        data = db.getAllRowsAsArrays();
        for (int position = 0; position < data.size(); position++) {
            row = data.get(position);
            Double quant = Double.parseDouble(row.get(3).toString());
            Double pri = Double.parseDouble(row.get(4).toString());
            Double total = quant * pri;
            sum = sum + total;
        }

        total.setText("$"+String.format("%.2f", sum));
        GrandTotal = sum;
        b.putString("sum", String.valueOf(sum));
    }

    private void updateTable() {
        while (dataTable.getChildCount() > 2) {
            dataTable.removeViewAt(2);
        }
        data = db.getAllRowsAsArrays();
        if(data.size() <= 0)
        {
            order.setBackgroundResource(R.drawable.check_out_black);
            order.setClickable(false);
        }
        for (int position = 0; position < data.size(); position++) {

            tableRow = new TableRow(this);
            tableRow.setId(toolid.generateViewId());
            id_list.add(tableRow.getId());
            tableRow.setPadding(0, 0, 0, 10);
            TableRow.LayoutParams params11 = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 50, 4.5f);
            tableRow.setLayoutParams(params11);
            tableRow.setMinimumHeight(20);
            row = data.get(position);

            idText = new TextView(this);
            idText.setTextSize(15);
            idText.setText("  " + row.get(0).toString());

            TextView textOne = new TextView(this);
            textOne.setTextSize(15);
            TableRow.LayoutParams params2 = new TableRow.LayoutParams(120, TableRow.LayoutParams.WRAP_CONTENT, 1.25f);
            textOne.setLayoutParams(params2);
            textOne.setText("  " + row.get(2).toString());
            textOne.setTextColor(getResources().getColor(R.color.light_hash));
            textOne.setMaxLines(1);
            textOne.setEllipsize(TextUtils.TruncateAt.END);
            tableRow.addView(textOne);

            EditText quant = new EditText(this);
            TableRow.LayoutParams paramsquant = new TableRow.LayoutParams(40, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            quant.setLayoutParams(paramsquant);
            quant.setBackgroundResource(R.drawable.edittext_back);
            quant.getEditableText();
            quant.setText("" + row.get(3).toString());
            quant.setGravity(Gravity.CENTER);
            quant.setId(Integer.parseInt(row.get(0).toString()));
            quant.setLabelFor(position);
            quant.setTextSize(15);

            View view = quant.findViewById(Integer.parseInt(row.get(0).toString()));
            quant.setFocusable(true);
            quant.setSelection(quant.getText().length());
            tableRow.addView(quant);
            quant.setInputType(InputType.TYPE_CLASS_NUMBER);
            quant.addTextChangedListener(new MyTextWatcher(view));

            textfour = new TextView(this);
            TableRow.LayoutParams paramsprice = new TableRow.LayoutParams(60, TableRow.LayoutParams.WRAP_CONTENT, 1.45f);
            paramsprice.setMargins(0, 0, 5, 0);
            textfour.setLayoutParams(paramsprice);
            textfour.setTextSize(15);
            textfour.setGravity(Gravity.RIGHT);

            textfour.setTextColor(getResources().getColor(R.color.light_hash));
            textfour.setMaxLines(1);
            textfour.setText(String.format("%.2f", Double.parseDouble(row.get(6).toString())));
            tableRow.addView(textfour);

            TextView text_dummy = new TextView(this);
            TableRow.LayoutParams params_dummy = new TableRow.LayoutParams(15, TableRow.LayoutParams.WRAP_CONTENT, 0.3f);
            params_dummy.setMargins(0, 0, 5, 0);
            text_dummy.setText(" ");
            tableRow.addView(text_dummy);

            close = new Button(this);
            TableRow.LayoutParams paramdelete = new TableRow.LayoutParams(50, TableRow.LayoutParams.WRAP_CONTENT, 0.5f);
            close.setLayoutParams(paramdelete);
            paramdelete.setMargins(5, 0, 10, 0);
            close.setText("X");
            close.setTextColor(getResources().getColor(R.color.white));
            close.setId(Integer.parseInt(row.get(0).toString()));
            close.setBackgroundResource(R.drawable.button_back);
            tableRow.addView(close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alterCart(v.getId());
                }

                private void alterCart(int id) {
                    try {
                        db.deleteRow(id);
                        updateTable();

                    } catch (Exception e) {
                        Log.e("Cart Exception", "hai");
                        e.printStackTrace();
                    }
                }
            });

            System.out.println("the id list " + id_list);
            dataTable.addView(tableRow);
        }
        Total_amount();
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            try {

                EditText editTextq = (EditText) findViewById(view.getId());
                editTextq.setFocusable(true);
                int position_new = editTextq.getLabelFor();
                row = data.get(position_new);
                String quantity = editTextq.getText().toString();
                int stock_quantity=Integer.parseInt(row.get(7).toString());
                if((Integer.parseInt(quantity)==0)){
                    checkout_flag=1;
                    order.setBackgroundResource(R.drawable.check_out_black);
                    order.setClickable(false);
                    editTextq.setTextColor(getResources().getColor(R.color.red));
                    Toast.makeText(getApplication(), "1 sec! You didn't add quantity", Toast.LENGTH_SHORT).show();
                }else {

                    if (Integer.parseInt(editTextq.getText().toString()) > stock_quantity) {
                        checkout_flag=1;
                        order.setBackgroundResource(R.drawable.check_out_black);
                        order.setClickable(false);
                        editTextq.setTextColor(getResources().getColor(R.color.red));
                        Toast.makeText(getApplicationContext(), "Enter available quantity", Toast.LENGTH_SHORT).show();

                    } else {
                        order.setBackgroundResource(R.drawable.check_out);
                        order.setClickable(true);
                        editTextq.setTextColor(getResources().getColor(R.color.dark_hash));
                        String total_new = String.valueOf(Integer.parseInt(quantity) * Double.parseDouble(row.get(4).toString()));
                        editTextq.setSelection(editTextq.getText().length());
                        db.updateRow(view.getId(), quantity, total_new);
                        updateTable();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onResume() {
        super.onResume();
        try {
            updateTable();
            Total_amount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

