# Snake Game

Java 21 ve Swing kullanılarak geliştirilen klasik yılan oyununun modernleştirilmiş sürümü.

## Özellikler

- Ok tuşları ve WASD ile kontrol
- Skor ve bilgisayarda saklanan en yüksek skor
- Yemle büyüyen yılan
- Mayınlar ve çarpışma kontrolü
- Altın coin: +50 puan
- Buz gücü: geçici yavaşlama
- Skora göre kontrollü hız artışı
- P veya ESC ile duraklatma
- Oyun bitti ekranı ve yeniden başlatma
- Başlangıç ipucu, puan animasyonu ve ölüm efekti
- Arka plan müziği
- Java kurulu olmayan bilgisayarlarda çalışabilecek paketleme desteği

## Mimari

Proje, katmanlı ve MVC benzeri bir yapı kullanır:

```text
src/
├── main/
│   ├── java/yilanoyunu/
│   │   ├── model/       # Oyun verileri, yönler ve durumlar
│   │   ├── engine/      # Hareket, çarpışma, skor ve oyun kuralları
│   │   ├── ui/          # Swing arayüzü, çizim, animasyon ve ses
│   │   └── YilanOyunu.java
│   └── resources/       # Görseller ve arka plan müziği
└── test/java/           # Oyun motoru testleri
```

`engine` paketi Swing arayüzünden bağımsız oyun kurallarını yönetir. `ui` paketi kullanıcı etkileşimini ve görsel sunumu sağlar. Bu ayrım, oyunun yeni özelliklerle büyütülmesini ve oyun kurallarının test edilmesini kolaylaştırır.

## Gereksinimler

- Java Development Kit 21 veya üzeri
- IntelliJ IDEA Community, Apache NetBeans veya başka bir Java IDE'si (isteğe bağlı)

JDK 21 kurulumu:

```powershell
winget install --id EclipseAdoptium.Temurin.21.JDK -e --source winget
```

## IntelliJ IDEA ile çalıştırma

1. Proje klasörünü IntelliJ IDEA ile açın.
2. `pom.xml` dosyasını Maven projesi olarak yükleyin.
3. JDK olarak Java 21'i seçin.
4. `src/main/java/yilanoyunu/YilanOyunu.java` dosyasını çalıştırın.

## Terminalden çalıştırma

```powershell
.\build.ps1
java -cp target/classes yilanoyunu.YilanOyunu
```

## Testleri çalıştırma

Maven kuruluysa:

```powershell
mvn test
```

## Java kurmadan çalıştırma

Geliştirici makinesinde paket oluşturmak için:

```powershell
.\package.ps1
```

Bu komut `dist/SnakeGame` altında Java runtime'ı gömülü taşınabilir uygulama ve `dist/SnakeGame-portable.zip` dosyasını oluşturur. ZIP'i açıp içindeki `SnakeGame.exe` dosyasına çift tıklamak yeterlidir; son kullanıcı bilgisayarında Java kurulması gerekmez.

İsteğe bağlı olarak `dist/SnakeGame.jar` dosyası da oluşturulur. JAR dosyasını çalıştırmak için bilgisayarda Java 21 bulunmalıdır:

```powershell
java -jar dist/SnakeGame.jar
```

## Oyun kontrolleri

| Tuş | İşlev |
|---|---|
| Ok tuşları / WASD | Yılanı hareket ettirir |
| P / ESC | Oyunu duraklatır veya devam ettirir |
| Yeniden Başlat | Oyun bittiğinde yeni oyun başlatır |

## Kullanılan dış asset

Altın coin görseli, [OpenGameArt üzerindeki CC0 altın coin sprite'ından](https://opengameart.org/content/gold-dollar-coin-glowing-2d-single-sprite) alınmıştır.

## Ekran Görüntüleri

![Oyun başlangıcı](assets/01-main-menu.png)

![Oynanış Animasyonu](assets/02-gameplay-demo.gif)

![Oynanış](assets/03-gameplay.png)

![Oyun Bitti](assets/04-game-over.png)
