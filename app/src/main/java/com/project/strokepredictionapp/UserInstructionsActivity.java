package com.project.strokepredictionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.project.strokepredictionapp.databinding.ActivityMainBinding;
import com.project.strokepredictionapp.databinding.ActivityUserInstructionsBinding;

public class UserInstructionsActivity extends AppCompatActivity {
    private ActivityUserInstructionsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInstructionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.tvUserInstructions.setPaintFlags(binding.tvUserInstructions.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.tvLink.setPaintFlags(binding.tvLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.tvLink.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.calculator.net/bmi-calculator.html"));
            startActivity(browserIntent);
        });
        binding.btnContinue.setOnClickListener(v -> startActivity(new Intent(UserInstructionsActivity.this,MainActivity.class)));
    }
}