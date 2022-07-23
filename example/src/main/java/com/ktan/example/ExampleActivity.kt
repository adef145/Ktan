package com.ktan.example

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ktan.annotations.Extras
import com.ktan.annotations.Mutable
import com.ktan.annotations.Required
import com.ktan.annotations.Route
import com.ktan.example.*
import com.ktan.binding.bindExtras
import com.ktan.extra.IntExtra
import com.ktan.extra.StringExtra
import com.ktan.parceler.extra.ParcelerExtra

@Route(
    extras = [ExampleExtras::class]
)
open class ExampleActivity : AppCompatActivity(R.layout.activity_example) {

    // Example how to binding the extras
    val extrasBinding: ExampleExtrasBinding by bindExtras()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    fun init() {
        val idEditText = findViewById<EditText>(R.id.id_extras)
        val nameEditText = findViewById<EditText>(R.id.name_extras)
        findViewById<Button>(R.id.next).apply {
            setOnClickListener {
                startActivityForResult(routeToExampleActivity {
                    id = idEditText.text.toString().toInt()
                    name = nameEditText.text.toString()
                    store = Store().also { store ->
                        store.id = id
                        store.name = name
                    }
                }, 1)
            }
        }
        findViewById<Button>(R.id.result).apply {
            setOnClickListener {
                setResult(Activity.RESULT_OK, Intent().populateExampleExtras {
                    id = idEditText.text.toString().toInt()
                })
                finish()
            }
        }

        initId(idEditText)
        nameEditText.setText(extrasBinding.store?.name)
    }

    fun initId(editText: EditText) {
        editText.setText(extrasBinding.store?.id.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val resultBinding: ExampleResultExtrasBinding? = data?.bindExtras()
        Toast.makeText(this, "This is the result ${resultBinding?.id}", Toast.LENGTH_SHORT).show()
    }
}

@Extras
class ExampleExtras {

    @Required
    // Example required extra
    val id = IntExtra("id", 0)

    @Mutable
    // Example option extra
    val name = StringExtra("name")

    // Example parceler extra
    val store = ParcelerExtra<Store>("store")
}

@Extras
class ExampleResultExtras {

    @Required
    // Example required extra
    val id = IntExtra("id", 0)
}