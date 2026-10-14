package com.example.textexample;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private String numero1 = "";
    private String operador = "";
    private String numero2 = "";

    private String lastOperador = "";
    private String lastNum = "";

    private boolean calculado = false;

    // Memoria de la calculadora
    private int memoria = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.textViewResultado);

        View.OnClickListener listener = view -> {
            String valor = view.getTag().toString();
            switch (valor) {
                case "0": case "1": case "2": case "3":
                case "4": case "5": case "6": case "7":
                case "8": case "9":
                    agregarNumero(valor);
                    break;
                case "plus": operacion("+"); break;
                case "minus": operacion("-"); break;
                case "multi": operacion("*"); break;
                case "divide": operacion("/"); break;
                case "equal": calcular(); break;
                case "memoPlus": memoriaSumar(); break;
                case "memoMinus": memoriaRestar(); break;
                case "memoShow": memoriaMostrar(); break;
                case "memoClear": memoriaLimpiar(); break;
            }
        };

        //region Listeners
        findViewById(R.id.btn_0).setOnClickListener(listener);
        findViewById(R.id.btn_1).setOnClickListener(listener);
        findViewById(R.id.btn_2).setOnClickListener(listener);
        findViewById(R.id.btn_3).setOnClickListener(listener);
        findViewById(R.id.btn_4).setOnClickListener(listener);
        findViewById(R.id.btn_5).setOnClickListener(listener);
        findViewById(R.id.btn_6).setOnClickListener(listener);
        findViewById(R.id.btn_7).setOnClickListener(listener);
        findViewById(R.id.btn_8).setOnClickListener(listener);
        findViewById(R.id.btn_9).setOnClickListener(listener);
        findViewById(R.id.btn_mas).setOnClickListener(listener);
        findViewById(R.id.btn_menos).setOnClickListener(listener);
        findViewById(R.id.btn_multi).setOnClickListener(listener);
        findViewById(R.id.btn_igual).setOnClickListener(listener);
        findViewById(R.id.btn_divide).setOnClickListener(listener);
        findViewById(R.id.btn_plusMemo).setOnClickListener(listener);
        findViewById(R.id.btn_MinusMemo).setOnClickListener(listener);
        findViewById(R.id.btn_MR).setOnClickListener(listener);
        findViewById(R.id.btn_MC).setOnClickListener(listener);

        //endregion
    }

    private void agregarNumero(String numero) {
        if (calculado) {
            numero1 = numero;
            numero2 = "";
            operador = "";
            textView.setText(numero1);
            calculado = false;
        } else if (operador.isEmpty()) {
            numero1 += numero;
        } else {
            numero2 += numero;
        }
        textView.setText(numero1 + (operador.isEmpty() ? "" : " " + operador + " " + numero2));
    }

    private void operacion(String op) {
        if (numero1.isEmpty()) {
            return;
        }

        if (!operador.isEmpty() && !numero2.isEmpty()) {
            calcularInterno();
        }

        if (calculado) {
            calculado = false;
        }

        operador = op;
        textView.setText(numero1 + " " + op);
    }

    private void calcular() {
        if (calculado && !lastOperador.isEmpty() && !lastNum.isEmpty()) {
            int n1 = Integer.parseInt(numero1);
            int n2 = Integer.parseInt(lastNum);
            int resultado = 0;

            switch (lastOperador) {
                case "+": resultado = n1 + n2; break;
                case "-": resultado = n1 - n2; break;
                case "*": resultado = n1 * n2; break;
                case "/": resultado = n1 / n2; break;
            }

            textView.setText(String.valueOf(resultado));
            numero1 = String.valueOf(resultado);
            return;
        }

        // Cálculo normal
        if (!numero1.isEmpty() && !numero2.isEmpty() && !operador.isEmpty()) {
            int n1 = Integer.parseInt(numero1);
            int n2 = Integer.parseInt(numero2);
            int resultado = 0;

            switch (operador) {
                case "+": resultado = n1 + n2; break;
                case "-": resultado = n1 - n2; break;
                case "*": resultado = n1 * n2; break;
                case "/": resultado = n1 / n2; break;
            }

            textView.setText(String.valueOf(resultado));

            // Guardar la última operación y operando
            lastOperador = operador;
            lastNum = numero2;

            numero1 = String.valueOf(resultado);
            numero2 = "";
            operador = "";
            calculado = true;
        }
    }

    private void calcularInterno() {
        if (!numero1.isEmpty() && !numero2.isEmpty() && !operador.isEmpty()) {
            int n1 = Integer.parseInt(numero1);
            int n2 = Integer.parseInt(numero2);
            int resultado = 0;

            switch (operador) {
                case "+": resultado = n1 + n2; break;
                case "-": resultado = n1 - n2; break;
                case "*": resultado = n1 * n2; break;
                case "/": resultado = n1 / n2; break;
            }

            numero1 = String.valueOf(resultado);
            numero2 = "";
            operador = "";
        }
    }

    // ===== Métodos de memoria =====

    // Obtiene el número que se está mostrando/editando
    private int valorActual() {
        String valor = !numero2.isEmpty() ? numero2 : numero1;
        return valor.isEmpty() ? 0 : Integer.parseInt(valor);
    }

    // M+: suma el valor actual a la memoria
    private void memoriaSumar() {
        memoria += valorActual();
        calculado = true;
    }

    // M-: resta el valor actual de la memoria
    private void memoriaRestar() {
        memoria -= valorActual();
        calculado = true;
    }

    // MR: muestra el valor guardado en memoria
    private void memoriaMostrar() {
        numero1 = String.valueOf(memoria);
        numero2 = "";
        operador = "";
        calculado = false;
        textView.setText(numero1);
    }

    // MC: borra la memoria
    private void memoriaLimpiar() {
        memoria = 0;
        calculado = true;
    }
}