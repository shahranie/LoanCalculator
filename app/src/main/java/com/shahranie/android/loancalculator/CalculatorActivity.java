package com.shahranie.android.loancalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        etLoanAmount = (EditText) findViewById(R.id.loan_amount);
        etDownPayment = (EditText) findViewById(R.id.down_payment);
        etTerm = (EditText) findViewById(R.id.term);
        etAnnualInterestRate = (EditText) findViewById(R.id.annual_interest_rate);

        tvMonthlyPayment = (TextView) findViewById(R.id.monthly_repayment);
        tvTotalRepayment = (TextView) findViewById(R.id.total_repayment);
        tvTotalInterest = (TextView) findViewById(R.id.total_interest);
        tvAverageMonthlyInterest = (TextView) findViewById(R.id.average_monthly_interest);

        // start for sharedPrefences - to display back

        SharedPreferences sp = getSharedPreferences(PREFS_CALCULATIONS, Context.MODE_PRIVATE);
        if(sp.getBoolean(HAS_RECORD, false)){
            tvMonthlyPayment.setText(sp.getString(MONTHLY_REPAYMENT,""));
            tvTotalRepayment.setText(sp.getString(TOTAL_REPAYMENT,""));
            tvTotalInterest.setText(sp.getString(TOTAL_INTEREST,""));
            tvAverageMonthlyInterest.setText(sp.getString(MONTHLY_INTEREST,""));

            etLoanAmount.setText(sp.getString(AMOUNT,""));
            etDownPayment.setText(sp.getString(DOWNPAYMENT,""));
            etTerm.setText(sp.getString(TERM,""));
            etAnnualInterestRate.setText(sp.getString(INTEREST_RATE,""));
        }

        // end for sharedPrefences
    }

    private EditText etLoanAmount, etDownPayment, etTerm, etAnnualInterestRate;
    private TextView tvMonthlyPayment, tvTotalRepayment, tvTotalInterest, tvAverageMonthlyInterest;

    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_calculate:
                calculate();
                Log.d("Check", "Button calculate clicked!");
                break;
            case R.id.button_reset:
                reset();
                Log.d("Check", "Button reset clicked!");
                break;
        }

    }
    // start for sharedPrefences
    public static  final String PREFS_CALCULATIONS = "LoanCalculation";
    public static  final String HAS_RECORD  = "hasRecord";
    public static final String MONTHLY_REPAYMENT = "monthly repayment";
    public static final String TOTAL_REPAYMENT = "total repayment";
    public static final String TOTAL_INTEREST = "total interest";
    public static final String MONTHLY_INTEREST = "monthly interest";

    public static final String AMOUNT = "amount";
    public static final String DOWNPAYMENT = "downpayment";
    public static final String INTEREST_RATE = "interest rate";
    public static final String TERM = "term rate";

    // end for sharedPrefences
    private void calculate(){
        String amount = etLoanAmount.getText().toString();
        String downPayment = etDownPayment.getText().toString();
        String interestRate = etAnnualInterestRate.getText().toString();
        String term = etTerm.getText().toString();

        double loanAmount = Double.parseDouble(amount)-Double.parseDouble(downPayment);
        double interest = Double.parseDouble(interestRate)/12/100;
        double noOfMonth = (Integer.parseInt(term) * 12);

        if(noOfMonth>0){
            double monthlyRepayment = loanAmount*(interest+(interest/(java.lang.Math.pow((1+interest),noOfMonth)-1)));
            double totalRepayment = monthlyRepayment * noOfMonth;
            double totalInterest = totalRepayment - loanAmount;
            double monthlyInterest = totalInterest / noOfMonth;

            DecimalFormat formatD = new DecimalFormat("#.00");//create decimal places method

            tvMonthlyPayment.setText(formatD.format(monthlyRepayment));
            tvTotalRepayment.setText(formatD.format(totalRepayment));
            tvTotalInterest.setText(formatD.format(totalInterest));
            tvAverageMonthlyInterest.setText(formatD.format(monthlyInterest));

            // start for sharedPrefences - to stored
            String mr = formatD.format(monthlyRepayment);
            String tr = formatD.format(totalRepayment);
            String ti = formatD.format(totalInterest);
            String mi = formatD.format(monthlyInterest);

            SharedPreferences sp = getSharedPreferences(PREFS_CALCULATIONS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putString(AMOUNT, amount);
            editor.putString(DOWNPAYMENT, downPayment);
            editor.putString(INTEREST_RATE, interestRate);
            editor.putString(TERM, term);

            editor.putString(MONTHLY_REPAYMENT, mr);
            editor.putString(TOTAL_REPAYMENT, tr);
            editor.putString(TOTAL_INTEREST, ti);
            editor.putString(MONTHLY_INTEREST, mi);
            editor.putBoolean(HAS_RECORD,true);
            editor.apply();
            // end for sharedPrefences

        }
    }

    private void reset() {
        etLoanAmount.setText("");
        etDownPayment.setText("");
        etTerm.setText("");
        etAnnualInterestRate.setText("");

        tvMonthlyPayment.setText(R.string.default_result);
        tvTotalRepayment.setText(R.string.default_result);
        tvTotalInterest.setText(R.string.default_result);
        tvAverageMonthlyInterest.setText(R.string.default_result);
    }


}
