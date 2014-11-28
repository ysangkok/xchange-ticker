fetch ivy
---

    wget -O ivy-2.3.0.jar "http://search.maven.org/remotecontent?filepath=org/apache/ivy/ivy/2.3.0/ivy-2.3.0.jar"
    unzip -p ivy-2.3.0.jar org/apache/ivy/core/settings/ivysettings.xml > ivysettings.xml

adjust `ivysettings.xml`:
---

**adjust `defaultResolver`:**

        <settings defaultResolver="chain"/>

**add inside `<ivysettings>`:**


    <resolvers>
        <chain name="chain" dual="true">
            <ibiblio name="sonatype" m2compatible="true" root="https://oss.sonatype.org/content/groups/public"/>
            <ibiblio name="example" m2compatible="true" root="http://mvn.erichseifert.de/maven2"/>
            <resolver ref="default"/>
        </chain>
    </resolvers>

fetch jars
---

    java -jar ivy-2.3.0.jar -settings ivysettings.xml -dependency com.xeiam.xchange xchange-examples 2.1.0 -retrieve "lib/[artifact]-[revision](-[classifier]).[ext]"

run (this will only work in zsh because of `**`)
---

    groovy -cp $(echo lib/**/*.jar | tr ' ' ':') ticker.groovy com.xeiam.xchange.coinfloor.CoinfloorExchange
