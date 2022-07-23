package com.ktan.example;

import com.ktan.annotations.Extras;
import com.ktan.annotations.Mutable;
import com.ktan.annotations.Required;
import com.ktan.extra.IntExtra;
import com.ktan.extra.StringExtra;
import com.ktan.parceler.extra.ParcelerExtra;

@Extras
final public class ExampleJavaExtras {

    // Example required extra
    @Required
    public final IntExtra id = new IntExtra("id", 0);

    @Mutable
    // Example option extra
    public final StringExtra name = new StringExtra("name");

    // Example parceler extra
    public final ParcelerExtra<Store> store = new ParcelerExtra<>("store");
}
