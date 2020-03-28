package com.example.assignment4;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Database db;
    TextView balance, history;
    EditText txtDate, txtPrice, txtItem;
    Button btnAdd, btnSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new Database(this);
        balance = findViewById(R.id.balance);
        txtDate = findViewById(R.id.editDate);
        txtPrice =  findViewById(R.id.editPrice);
        txtItem =  findViewById(R.id.editItem);

        btnAdd = findViewById(R.id.btnAdd);
        btnSub = findViewById(R.id.btnSub);

        history = findViewById(R.id.historyContent);
        AddTransaction();
        GetHistory();
    }

    public void AddTransaction(){
        btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double price = Double.parseDouble(txtPrice.getText().toString());
                        boolean result = db.createTransaction(txtItem.getText().toString(), txtDate.getText().toString(), price);
                        if (result)
                            Toast.makeText(MainActivity.this, "Transaction Created", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Transaction Not Created", Toast.LENGTH_LONG).show();
                        GetHistory();
                        ClearText();
                    }
                }
        );

        btnSub.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double price = -1 * Double.parseDouble(txtPrice.getText().toString());
                        boolean result = db.createTransaction(txtItem.getText().toString(), txtDate.getText().toString(), price);
                        if (result)
                            Toast.makeText(MainActivity.this, "Transaction Created", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this, "Transaction Not Created", Toast.LENGTH_LONG).show();
                        GetHistory();
                        ClearText();
                    }
                }
        );
    }

    public void GetHistory(){
        Cursor result = db.getAllData();
        StringBuffer buffer = new StringBuffer();
        Double balance = 0.0;
        while(result.moveToNext()){
            // get price string to remove negative sign
            String priceString = result.getString(3);
            double price = Double.parseDouble(result.getString(3));
            balance += price;
            if (price < 0) {
                buffer.append("Spent $");
                priceString = priceString.substring(1);
            }
            else { buffer.append("Added $");}
            buffer.append(priceString + " on " + result.getString(2) +
                    " for " + result.getString(1) + "\n");
        }
        MainActivity.this.balance.setText("Current Balance: $" + Double.toString(balance));
        MainActivity.this.history.setText(buffer);
    }

    public void ClearText(){
        MainActivity.this.txtDate.setText("");
        MainActivity.this.txtPrice.setText("");
        MainActivity.this.txtItem.setText("");
    }
}