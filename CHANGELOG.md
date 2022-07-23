# Changelog

## 1.0
### Release Highlights
This initial version, we try to make easier and simpler to get and put intent extras / arguments.
To make it come true, we create some convention to generated some boilerplate function, so we don't need do and write the same things again and again

### Features
* Annotation `@Extras`. To help processor to generate `Binding` and `Routing` class, and some extension function to populate extras to intent / bundle.
  * Annotation `@Required`. To define non null extra.
  * Annotation `@Mutable`. To define extra can modified (var).
* Annotation `@Route`. To help processor to generate some extension function to help us to know which extras need to pass to route particular Activity / Fragment.
  * Property `extras`. To define for particular `@Route` need several `extras`
* Having `bindExtras` extension function in `Activity` and `Fragment`.
* Having basic Extra class such as:
  * BinderExtra
  * BooleanExtra
  * BundleExtra
  * ByteArrayExtra
  * ByteExtra
  * CharArrayExtra
  * CharExtra
  * CharSequenceArrayExtra
  * CharSequenceExtra
  * DoubleArrayExtra
  * DoubleExtra
  * FloatArrayExtra
  * FloatExtra
  * IntArrayExtra
  * IntExtra
  * LongArrayExtra
  * LongExtra
  * ParcelableArrayExtra
  * ParcelableExtra
  * SerializableExtra
  * ShortArrayExtra
  * ShortExtra
  * SizeExtra
  * SizeFExtra
  * StringArrayExtra
  * StringArrayListExtra
  * StringExtra
* Having ParcelerExtra to integrate with [johncarl81/parceler](https://github.com/johncarl81/parceler)
