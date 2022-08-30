package com.ktan.example;

import static com.ktan.example.ExampleActivityJavaRouteKt.routeToExampleActivityJava;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ktan.annotations.Route;
import com.ktan.binding.ExtrasBindingKt;

import kotlin.Unit;

@Route(
        extras = {ExampleJavaExtras.class}
)
public class ExampleActivityJava extends AppCompatActivity {

    private final ExampleJavaExtrasBinding extrasBinding = new ExampleJavaExtrasBinding();

    public ExampleActivityJava() {
        super(R.layout.activity_example);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExtrasBindingKt.bindExtras(this, extrasBinding);

        EditText idEditText = findViewById(R.id.id_extras);
        EditText nameEditText = findViewById(R.id.name_extras);
        Button nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(view -> {
            startActivityForResult(routeToExampleActivityJava(this, router -> {
                router.setId(Integer.parseInt(idEditText.getText().toString()));
                router.setName(nameEditText.getText().toString());
                Store store = new Store();
                store.setId(Integer.parseInt(idEditText.getText().toString()));
                store.setName(nameEditText.getText().toString());
                router.setStore(store);
                return Unit.INSTANCE;
            }), 1);
        });

        Button resultButton = findViewById(R.id.result);
        resultButton.setOnClickListener(view -> {
            setResult(Activity.RESULT_OK, ExampleResultJavaExtrasKtanKt.populateExampleResultJavaExtras(new Intent(), router -> {
                router.setId(Integer.parseInt(idEditText.getText().toString()));
                return Unit.INSTANCE;
            }));
            finish();
        });

        if (extrasBinding.getStore() != null) {
            idEditText.setText(String.valueOf(extrasBinding.getStore().getId()));
            nameEditText.setText(extrasBinding.getStore().getName());
        } else {
            idEditText.setText(String.valueOf(extrasBinding.getId()));
            nameEditText.setText(extrasBinding.getName());
        }
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                extrasBinding.setName(editable.toString());
                extrasBinding.getNameLive().postValue(editable.toString());
            }
        });
        extrasBinding.getNameLive().observe(this, name -> {
            if (name == null) {
                return;
            }
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ExampleResultJavaExtrasBinding resultBinding = ExtrasBindingKt.bindExtras(data, new ExampleResultJavaExtrasBinding());
        Toast.makeText(this, "This is the result " + resultBinding.getId(), Toast.LENGTH_SHORT).show();
    }
}
