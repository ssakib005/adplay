# AdPlay
AdPlay is an Android SDK for dealing with any kind of advertisement in your application.

#Download

use Gradle:

-<b>Project Level build.gradel</b></br>

repositories {
  ..</br>
  maven { url 'https://jitpack.io' } <br>
}<br>
-<b>App Level build.gradel</b>

dependencies {
  implementation 'com.github.ssakib005:adplay:0.1.0'
}

#How do I use AdPlay?

Simple use cases will look something like this:

<b>Add String Resource</b>
   <string name="tag"><![CDATA[<ins class=\"adplay_pmp_content_121\" data-ad-client=\"583d67557b8f3\" data-ad-iab=\"IAB11-1\"
   data-ad-w=\"300\" data-ad-h=\"250\" data-ad-pt=\"web\" data-ad-c_type=\"1\"
   data-clickurl=\"%%CLICK_URL_UNESC%%CMP_CLICKURL\" data-auction=\"${AUCTION_PRICE}\"
   style=\"display:block;text-align: center; margin:0 auto;\"></ins>]]></string>
   

<b>Add Layout</b>
  <com.ads.AdPlay
      android:layout_width="0dp"
      android:layout_height="0dp"
      android:id="@+id/adsId"
      android:layout_gravity="center" />

<b>Initialize using Java or Kotlin</b>

class MainActivity : AppCompatActivity() {

    private lateinit var adplay: AdPlay
    var tag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        tag = getString(R.string.tag)
        adplay = findViewById(R.id.adsId)
        adplay.addTag(this,tag).load()
    }
}



#Status
Version 0.1.0 is now released and stable. Updates are released periodically with new features and bug fixes.

Comments/bugs/questions/pull requests are always welcome! Please read CONTRIBUTING.md on how to report issues.

#Compatibility
Minimum Android SDK: AdPlay requires a minimum API level of 16.
Compile Android SDK: Adplay requires you to compile against API 29 or later.
