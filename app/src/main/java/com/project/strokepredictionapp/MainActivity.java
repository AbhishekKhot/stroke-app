package com.project.strokepredictionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.project.strokepredictionapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private final String url = "https://strokeapi.onrender.com/result";
    private ActivityMainBinding binding;
    private String gender;
    private String age;
    private String hypertension;
    private String heart_disease;
    private String ever_married;
    private String work_type;
    private String residence_type;
    private String avg_glucose_level;
    private String bmi;
    private String smoking_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        String[] gendersArray;
        gendersArray = getResources().getStringArray(R.array.spinner_gender_type);
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, R.layout.dropdown_menu, gendersArray);
        binding.TextViewGender.setAdapter(arrayAdapter1);

        String[] optionsArray;
        optionsArray = getResources().getStringArray(R.array.spinner_yes_no);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, R.layout.dropdown_menu, optionsArray);
        binding.TextViewHeartDisease.setAdapter(arrayAdapter2);
        binding.TextViewHypertension.setAdapter(arrayAdapter2);
        binding.TextViewEverMarried.setAdapter(arrayAdapter2);

        String[] workTypeArray;
        workTypeArray = getResources().getStringArray(R.array.spinner_work_type);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(this, R.layout.dropdown_menu, workTypeArray);
        binding.TextViewWorkType.setAdapter(arrayAdapter3);

        String[] residenceType;
        residenceType = getResources().getStringArray(R.array.spinner_residence_type);
        ArrayAdapter arrayAdapter4 = new ArrayAdapter(this, R.layout.dropdown_menu, residenceType);
        binding.TextViewResidenceType.setAdapter(arrayAdapter4);

        String[] smokingStatusArray;
        smokingStatusArray = getResources().getStringArray(R.array.spinner_smoking_status);
        ArrayAdapter<String> arrayAdapter5 = new ArrayAdapter<>(this, R.layout.dropdown_menu, smokingStatusArray);
        binding.TextViewSmokingStatus.setAdapter(arrayAdapter5);

        binding.btnPredict.setOnClickListener(v -> {
            if(checkValues()){
                encodeGender();
                encodeHeartDisease();
                encodeEverMarried();
                encodeHypertension();
                encodeWorkType();
                encodeResidenceType();
                encodeSmokingStatus();
                age= Objects.requireNonNull(binding.UserAge.getText()).toString().trim();

                //change these values if they are not precise
                avg_glucose_level= Objects.requireNonNull(binding.UserGlucoseLevel.getText()).toString().trim();
                bmi= Objects.requireNonNull(binding.UserBMI.getText()).toString().trim();
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        response -> {
                            try {
                                progressDialog.cancel();
                                JSONObject jsonObject = new JSONObject(response);
                                String ans = jsonObject.getString("PredictionResult");
                                if(ans.equals("[0]")){
//                                    Toast.makeText(MainActivity.this,"No chance",Toast.LENGTH_LONG).show();
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                                    builder1.setMessage("You don't any chances of getting stroke in near future.");
                                    builder1.setCancelable(true);

                                    builder1.setPositiveButton(
                                            "Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    builder1.setNegativeButton(
                                            "",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();
                                }
                                else{
//                                    Toast.makeText(MainActivity.this,"Have chance",Toast.LENGTH_LONG).show();
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                                    builder1.setMessage("You have chances of getting stroke, We suggest you to consult with the doctor.");
                                    builder1.setCancelable(true);

                                    builder1.setPositiveButton(
                                            "Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                }
                                            });

                                    builder1.setNegativeButton(
                                            "",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();
                                }
                                Log.d("API_RESPONSE", ans);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> error.printStackTrace()) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("gender", gender);//int 1
                        params.put("age", age);//int 67
                        params.put("hypertension", hypertension);//int 0
                        params.put("heart_disease", heart_disease);//int 1
                        params.put("ever_married", ever_married);//int 1
                        params.put("work_type", work_type);//int 2
                        params.put("Residence_type", residence_type);//int 1
                        params.put("avg_glucose_level", avg_glucose_level);//float 228.69
                        params.put("bmi", bmi); //float 36.600000
                        params.put("smoking_status", smoking_status);//int 1
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(stringRequest);

            }
            else{
                Toast.makeText(MainActivity.this,"Please make sure you have selected all required fields.",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void encodeSmokingStatus() {
        if(binding.TextViewSmokingStatus.getText().toString().trim().equals("Never Smoked")){
            smoking_status="2";
        }
        else if(binding.TextViewSmokingStatus.getText().toString().trim().equals("Daily Smoker")){
            smoking_status="0";
        }
        else if(binding.TextViewSmokingStatus.getText().toString().trim().equals("Formerly Smoked")){
            smoking_status="1";
        }
    }

    private void encodeResidenceType() {
        if(binding.TextViewResidenceType.getText().toString().trim().equals("Urban")){
            residence_type="1";
        }
        else if(binding.TextViewResidenceType.getText().toString().trim().equals("Rural")){
            residence_type="0";
        }
    }

    private void encodeWorkType() {
        if(binding.TextViewWorkType.getText().toString().trim().equals("Private Job")){
            work_type="2";
        }
        else if(binding.TextViewWorkType.getText().toString().trim().equals("Government Job")){
            work_type="0";
        }
        else if(binding.TextViewWorkType.getText().toString().trim().equals("Self Employed")){
            work_type="3";
        }
        else if(binding.TextViewWorkType.getText().toString().trim().equals("Never Worked")){
            work_type="1";
        }
        else if(binding.TextViewWorkType.getText().toString().trim().equals("Student")){
            work_type="1";
        }
    }

    private void encodeHypertension() {
        if(binding.TextViewHypertension.getText().toString().trim().equals("Yes")){
            hypertension="1";
        }
        else if(binding.TextViewHypertension.getText().toString().trim().equals("No")){
            hypertension="0";
        }
    }

    private void encodeEverMarried() {
        if(binding.TextViewEverMarried.getText().toString().trim().equals("Yes")){
            ever_married="1";
        }
        else if(binding.TextViewEverMarried.getText().toString().trim().equals("No")){
            ever_married="0";
        }
    }

    private void encodeHeartDisease() {
        if(binding.TextViewHeartDisease.getText().toString().trim().equals("Yes")){
            heart_disease="1";
        }
        else if(binding.TextViewHeartDisease.getText().toString().trim().equals("No")){
            heart_disease="0";
        }
    }

    private void encodeGender() {
        if(binding.TextViewGender.getText().toString().trim().equals("Male")){
            gender="1";
        }
        else if(binding.TextViewGender.getText().toString().trim().equals("Female")){
            gender="0";
        }
    }

    private boolean checkValues(){
        return (!binding.UserAge.getText().toString().isEmpty() &&
                !binding.UserBMI.getText().toString().isEmpty() &&
                !binding.UserGlucoseLevel.getText().toString().isEmpty()) &&
                (!binding.TextViewGender.getText().toString().equals("Choose your Gender") &&
                        !binding.TextViewHeartDisease.getText().toString().equals("Do you have any heart disease") &&
                        !binding.TextViewEverMarried.getText().toString().equals("Have you ever married") &&
                        !binding.TextViewHypertension.getText().toString().equals("Do you have hypertension") &&
                        !binding.TextViewWorkType.getText().toString().equals("What type of work you do ?") &&
                        !binding.TextViewResidenceType.getText().toString().equals("What kind of area do you live ?") &&
                        !binding.TextViewSmokingStatus.getText().toString().equals("what is your smoking status ?"));
    }
}