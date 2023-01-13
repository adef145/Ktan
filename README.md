# Ktan
[![](https://jitpack.io/v/adef145/Ktan.svg)](https://jitpack.io/#adef145/Ktan)
![GitHub](https://img.shields.io/github/license/adef145/Ktan)

Ktan make your intent / arguments more easier and readable. And most important, will help you bind  all extras for your activity / fragment. And also support for java :heart:.

## How to use

1. Add jitpack repository at root gradle
   ```groovy  
   allprojects {  
        repositories { 
            maven { url 'https://jitpack.io' } 
        }
   }  
   ```  
2. Implement our library
   ```groovy  
   // in your root gradle
   buildscript {
      dependencies {
         ...
         // add this classpath to use ksp
         classpath "com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:$ksp_version"
      }
   }
   
   // in your app gradle
   plugins {
      ...
      // Add this to implement ksp
      id 'com.google.devtools.ksp'
   }
   
   ksp {
      // THIS OPTION IS REMOVED
      // arg("com.ktan.processor.LIVE_DATA", "true")

      // put this arg to turn on LiveData for all extra
      arg("com.ktan.processor.integrations", "livedata")
      // or
      // put this arg to turn on Flow for all extra
      arg("com.ktan.processor.integrations", "flow")
   }
   
   android {
      buildTypes {
         release {
            sourceSets {
               main {
                  // Add this to implement ksp
                  java { srcDirs = ["src/main", "build/generated/ksp/release"] }
               }
            }
         }
         debug {
            sourceSets {
               main {
                  // Add this to implement ksp
                  java { srcDirs = ["src/main", "build/generated/ksp/debug"] }
               }
            }
         }
      }
   }
   
   dependencies {  
        // Ktan implementation
        implementation "com.github.adef145.Ktan:ktan:$latest_version"
        ksp "com.github.adef145.Ktan:ktan-processor:$latest_version"  
        
        // Optional: Parceler integration implementation
        implementation "com.github.adef145.Ktan:ktan-parceler:$latest_version"
   
        // Optional: LiveData integration implementation
        implementation("com.github.adef145.Ktan:ktan-livedata:$latest_version") {
            // put this line in case your targetSdkVersion below 32
            exclude group: 'androidx.appcompat'
        }

        // Optional: Flow integration implementation
        implementation("com.github.adef145.Ktan:ktan-flow:$latest_version")
   }  
   ```  
   For more detail about ksp, you can read in [here](https://kotlinlang.org/docs/ksp-overview.html)
3. Init Ktan on your `Application.onCreate`  
   
   *Kotlin*
   ```kotlin  
   class YourApplication : Application() {   
        
        override fun onCreate() { 
            Ktan.init(this) 
        }
   }  
   ```  
   
   *Java*
   ```java  
   public class YourApplication extends Application {  
       @Override  
       public void onCreate() { 
           Ktan.init(this); 
       }
   }  
   ```  
4. Create extras for your Activity  
   
   *Kotlin*
   ```kotlin  
   @Extras
   class YourActivityExtras {  
        
        @Required // to define this extra is required and non null
        @Mutable // to define this extra is mutable (var)
        // define extra with default value 
        val id = IntExtra("id_extra", 0) 
        
        // define extra without default value and nullable 
        val name = StringExtra("name_extra")
   
        @Mutable // to define MutableLiveData instead of LiveData
        @Required // to define this extra is required and non null when observe
        @LiveExtra // to define this extra as LiveData when Binding 
        val nameLive = StringExtra("name_live_extra")

        // All flow will be map to StateFlow or MutableStateFlow
        @Mutable // to define MutableStateFlow instead of StateFlow
        @Required // to define this extra is required and non null when observe
        @FlowExtra // to define this extra as StateFlow when Binding 
        val nameFlow = StringExtra("name_flow_extra")
   }  
   ```  
   
   *Java*
   ```java
   @Extras
   public class YourActivityExtras {  
        
        @Required // to define this extra is required and non null
        @Mutable // to define this extra is mutable (var)
        // define extra with default value 
        public IntExtra id = new IntExtra("id_extra", 0); 
        
        // define extra without default value and nullable 
        public StringExtra name = StringExtra("name_extra");
   
        @Mutable // to define MutableLiveData instead of LiveData
        @Required // to define this extra is required and non null when observe
        @LiveExtra // to define this extra as LiveData when Binding
        public StringExtra nameLive = StringExtra("name_extra");
   }  
   ```  
   With this class, ksp processor will create one file with name `YourActivityExtrasKtan`.
   And in this file, will contains 2 class `YourActivityExtrasBinding` and `YourActivityExtrasRouter`,
   also contains 2 extension function `Intent.populateYourActivityExtras` and `Bundle.populateYourActivityExtras`
   ```kotlin
   class YourActivityExtrasBinding(extras: YourActivityExtras = YourActivityExtras()) : ExtrasBinding() {
        
        var id: Int by requiredExtraOf(extras.id)
        val name: String? by extraOf(extas.name) // by default all extra is nullable if not annotated with Required
        val nameLive: MutableLiveData<String> by mutableLiveExtraOf(extas.name)
        val nameFlow: MutableStateFlow<String> by mutableFlowExtraOf(extas.name)
        
   }
   
   class YourActivityExtrasRouter(block: YourActivityExtras.() -> Unit) : KtanRouter() {
   
        var id: Int by requiredExtraOf(extras.id)
        var name: String? by extraOf(extas.name)
        var nameLive: String by extraOf(extas.nameLive)
        var nameFlow: String by extraOf(extas.nameFlow)
   
        init {
            block.invoke(this)
        }
   
   }
   
   fun Intent.populateYourActivityExtras(block: YourActivityExtrasRouter.() -> Unit): Intent {
      YourActivityExtrasRouter(block).populate(this)
      return this
   }
   
   fun Bundle.populateYourActivityExtras(block: YourActivityExtrasRouter.() -> Unit): Bundle {
      YourActivityExtrasRouter(block).populate(this)
      return this
   }
   ```
5. Bind your extras binding to your activity. And annotate with `@Route` in YourActivity
   
   *Kotlin*
   ```kotlin
   // Annotate this activity to create route file
   @Route(
      // define extras that will work for open YourActivity. Can multiple
      extras = [YorActivityExtras::class]
   )
   class YourActivity : AppCompatActivity() {  
        
        val extrasBinding: YourActivityExtrasBinding by bindExtras()  
   }  
   ```  
   
   *Java*
   ```java
   // Annotate this activity to create route file
   @Route(
        // define extras that will work for open YourActivity. Can multiple
        extras = {YourActivityExtras.class}
   )
   public class YourActivity extends AppCompatActivity {  
        
        private final YourActivityExtrasBinding extrasBinding = new YourActivityExtrasBinding();  
        
        @Override  
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            ExtrasBindingKt.bindExtras(this, extrasBinding); 
        }
   }  
   ```
   With route annotation, the processor will create a file `YourActivityRoute`, and contains extension function to route this activity
   ```kotlin
   fun routeToYourActivity(
        context: Context, 
        yourActivityExtrasRouterBlock: YourActivityExtrasRouter.() -> Unit): Intent {
        return = Intent(context, YourActivity::class)
            .populateYourActivityExtras(yourActivityExtrasRouterBlock)
   }
   
   fun Activity.routeToYourActivity( 
        yourActivityExtrasRouterBlock: YourActivityExtrasRouter.() -> Unit): Intent {
        return routeToExampleActivity(this, yourActivityExtrasRouterBlock)
   }
   
   fun Fragment.routeToYourActivity( 
        yourActivityExtrasRouterBlock: YourActivityExtrasRouter.() -> Unit): Intent? {
        return context?.let { routeToExampleActivity(it, yourActivityExtrasRouterBlock) }
   }
   ```
   You can use this extension function to open `YourActivity`
6. How to use your router  
   
   *Kotlin*
   ```kotlin  
   class YourActivity : AppCompatActivity() {  

        fun someMethod() {  
            startActivity(
               Intent(context, YourActivity::class).populateYourActivityExtras {
                  id = 1
                  name = "Set your name here"
                  nameLive = "Set your nameLive here"
                  nameFlow = "Set your nameFlow here"
               }
            }
            // or you can use extention function that created from @Route annotation
            startActivity(routeToYourActivity {
               id = 1
               name = "Set your name here"   
               nameLive = "Set your name live here"
               nameFlow = "Set your name flow here"
            })
        }  
   ```  
   
   *Java*
   ```java  
   public class YourActivity extends AppCompatActivity {  

        public void someMethod() {  
            startActivity(
               YourActivityExtrasKtanKt
                  .populateYourActivityExtras(new Intent(context, YourActivity.class), router -> {
                     router.setId(1);
                     router.setName("Set your name here");
                     router.setNameLive("Set your name live here");
                     return Unit.INSTANCE;
                  })
            ); 
            // or you can use extention function that created from @Route annotation
            startActivity(YourActivityRouteKt.routeToYourActivity(router -> {
               router.setId(1);
               router.setName("Set your name here");
               router.setNameLive("Set your name live here");
               return Unit.INSTANCE;
            }));
        }
   }  
   ```  
7. You can also use Ktan for your activity result
   
   *Kotlin*
   ```kotlin
   class YourActivity : AppCompatActivity() {
   
        fun setResultForPreviousActivity() {
            setResult(Activity.RESULT_OK, Intent().populateExampleActivityResultExtras {
                id = idEditText.text.toString().toInt()
            })
        }
   
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            val resultBinding: ExampleActivityResultExtrasBinding? = data?.bindExtras()
            Toast.makeText(this, "This is the result ${resultBinding?.id}", Toast.LENGTH_SHORT).show()
        }
   }  
   
   @Extras
   class ExampleActivityResultExtras {
      val id = IntExtras("id", 0)
   }
   ```  
   
   *Java*
   ```java
   public class YourActivity extends AppCompatActivity {
   
        public void setResultForPreviousActivity() {
            setResult(Activity.RESULT_OK, ExampleActivityResultExtrasKtanKt.populateExampleActivityResultExtras(new Intent(), router -> {
                router.setId(1);
                return Unit.INSTANCE;
            }));
        }
   
        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            ExampleActivityResultExtrasBinding resultBinding = ExtrasBindintKt.bindExtras(data, new ExampleActivityResultExtrasBinding());
            Toast.makeText(this, "This is the result " + resultBinding.getId(), Toast.LENGTH_SHORT).show();
        }
   }  
   
   @Extras
   public class ExampleActivityResultExtras {
      public IntExtras id = new IntExtras("id", 0);
   }
   ```

Yap, that all how to use Ktan for your project. And no need handle for savedInstanceState,  because for mutable extra with `var` by default will handle your `onSavedInstanceState`
And also, will work for *Fragment*.

```markdown
MIT License

Copyright (c) 2022 Ade Fruandta

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
