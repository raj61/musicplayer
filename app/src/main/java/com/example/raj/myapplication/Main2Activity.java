package com.example.raj.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    Button btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btnadd,btnequal,btnc,btnminus,btnmul,btndivide;
    TextView result;
    int ans=0,num=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        result=(TextView)findViewById(R.id.result);
        btn0=(Button)findViewById(R.id.btn0);
        btn1=(Button)findViewById(R.id.btn1);
        btn2=(Button)findViewById(R.id.btn2);
        btn3=(Button)findViewById(R.id.btn3);
        btn4=(Button)findViewById(R.id.btn4);
        btn5=(Button)findViewById(R.id.btn5);
        btn6=(Button)findViewById(R.id.btn6);
        btn7=(Button)findViewById(R.id.btn7);
        btn8=(Button)findViewById(R.id.btn8);
        btn9=(Button)findViewById(R.id.btn9);
        btnadd=(Button)findViewById(R.id.btnadd);
        btnequal=(Button)findViewById(R.id.btnequal);
        btnc=(Button)findViewById(R.id.btnc);
        btnminus=(Button)findViewById(R.id.btnminus);
        btnmul=(Button)findViewById(R.id.btnmul);
        btndivide=(Button)findViewById(R.id.btndivide);
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnadd.setOnClickListener(this);
        btnequal.setOnClickListener(this);
        btnc.setOnClickListener(this);
        btnminus.setOnClickListener(this);
        btnmul.setOnClickListener(this);
        btndivide.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int btn = v.getId();
            if(btn == R.id.btn0)
            {
                num = num*10+0;
                result.setText(""+num);
            }
            else if(btn == R.id.btn1)
            {
                num = num*10+1;
                result.setText(""+num);
            }
            else if(btn == R.id.btn2)
            {
                num = num*10+2;
                result.setText(""+num);
            }
            else if(btn == R.id.btn3)
            {
                num = num*10+3;
                result.setText(""+num);
            }
            else if(btn == R.id.btn4)
            {
                num = num*10+4;
                result.setText(""+num);
            }
            else if(btn == R.id.btn5)
            {
                num = num*10+5;

                result.setText(""+num);
            }
            else if(btn == R.id.btn6)
            {
                num = num*10+6;
                result.setText(""+num);
            }
            else if(btn == R.id.btn7)
            {
                num = num*10+7;
                result.setText(""+num);
            }
            else if(btn == R.id.btn8)
            {
                num = num*10+8;
                result.setText(""+num);
            }
            else if(btn == R.id.btn9)
            {
                num = num*10+9;
                result.setText(""+num);
            }
            else if(btn == R.id.btnadd)
            {
                ans = ans+num;
                num=0;
                result.setText("");
            }
            else if(btn == R.id.btnminus)
            {
                ans = ans-num;
                num = 0;
                result.setText("");
            }
            else if(btn == R.id.btnmul)
            {
                ans = ans*num;
                num = 0;
                result.setText("");
            }
            else if(btn == R.id.btndivide)
            {
                if(num == 0)
                {
                    ans = 0;
                    num = 0;
                    result.setText("Invalid operation");
                }
                else {
                    ans = ans / num;
                    num = 0;
                    result.setText("");
                }
            }

            else if(btn == R.id.btnequal)
            {
                ans = ans+num;
                num=0;
                result.setText(""+ans);
                num = ans;
                ans = 0;
            }
            else if(btn == R.id.btnc)
            {
                ans =0;
                num =0;
                result.setText(""+ans);
            }

    }
}
