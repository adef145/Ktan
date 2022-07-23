package com.ktan.example

import com.ktan.annotations.Route

@Route(
    extras = [ExampleExtras::class]
)
open class ExampleActivity3 : ExampleActivity2<Any>() {

}