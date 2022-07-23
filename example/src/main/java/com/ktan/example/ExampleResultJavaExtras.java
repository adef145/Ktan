package com.ktan.example;

import com.ktan.annotations.Extras;
import com.ktan.annotations.Required;
import com.ktan.extra.IntExtra;

@Extras
final public class ExampleResultJavaExtras {

    // Example required extra
    @Required
    public final IntExtra id = new IntExtra("id", 0);
}
