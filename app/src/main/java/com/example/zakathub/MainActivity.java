package com.example.zakathub;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    EditText etWeight, etPrice;
    RadioGroup rgGoldType;
    Button btnCalculate;
    TextView tvGoldValue, tvZakatPayable, tvTotalZakat, tvNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set ActionBar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Zakat Hub");
        }

        getSupportActionBar().setTitle("Zakat Hub");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#81C784"))
        );

        etWeight       = findViewById(R.id.etWeight);
        etPrice        = findViewById(R.id.etPrice);
        rgGoldType     = findViewById(R.id.rgGoldType);
        btnCalculate   = findViewById(R.id.btnCalculate);
        tvGoldValue    = findViewById(R.id.tvGoldValue);
        tvZakatPayable = findViewById(R.id.tvZakatPayable);
        tvTotalZakat   = findViewById(R.id.tvTotalZakat);
        tvNotice       = findViewById(R.id.tvNotice);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateZakat();
            }
        });
    }

    private void calculateZakat() {
        String weightStr = etWeight.getText().toString().trim();
        String priceStr  = etPrice.getText().toString().trim();

        // Validate inputs
        if (weightStr.isEmpty()) {
            etWeight.setError("Please enter gold weight");
            return;
        }
        if (priceStr.isEmpty()) {
            etPrice.setError("Please enter gold price");
            return;
        }

        double weight = Double.parseDouble(weightStr);
        double price  = Double.parseDouble(priceStr);

        if (weight <= 0) {
            etWeight.setError("Weight must be greater than 0");
            return;
        }
        if (price <= 0) {
            etPrice.setError("Price must be greater than 0");
            return;
        }

        // Determine nisab based on gold type
        int selectedId = rgGoldType.getCheckedRadioButtonId();
        double nisab = (selectedId == R.id.rbKeep) ? 85.0 : 200.0;

        // Calculations
        double totalGoldValue   = weight * price;
        double zakatableWeight  = weight - nisab;
        double zakatPayable     = (zakatableWeight > 0) ? zakatableWeight * price : 0;
        double totalZakat       = zakatPayable * 0.025;

        // Show results
        tvGoldValue.setText(String.format("Total Gold Value: RM %.2f", totalGoldValue));
        tvZakatPayable.setText(String.format("Zakat Payable Amount: RM %.2f", zakatPayable));
        tvTotalZakat.setText(String.format("Total Zakat (2.5%%): RM %.2f", totalZakat));

        tvGoldValue.setVisibility(View.VISIBLE);
        tvZakatPayable.setVisibility(View.VISIBLE);
        tvTotalZakat.setVisibility(View.VISIBLE);

        // Show notice if below nisab
        if (zakatableWeight <= 0) {
            tvNotice.setText("⚠ Your gold is below the nisab threshold. No zakat is required.");
            tvNotice.setVisibility(View.VISIBLE);
        } else {
            tvNotice.setVisibility(View.GONE);
        }
    }

    // ActionBar menu (share button)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            // Share the GitHub URL
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Zakat Gold Calculator");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "Check out my Zakat Gold Calculator app!\n\nhttps://github.com/Ac-Ey/ZakatHub");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        }
        if (item.getItemId() == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}