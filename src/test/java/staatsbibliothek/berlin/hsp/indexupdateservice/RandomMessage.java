package staatsbibliothek.berlin.hsp.indexupdateservice;

import static java.lang.Math.abs;
import static net.andreinc.mockneat.types.enums.MarkovChainType.KAFKA;
import static net.andreinc.mockneat.types.enums.MarkovChainType.LOREM_IPSUM;
import static net.andreinc.mockneat.unit.address.Cities.cities;
import static net.andreinc.mockneat.unit.companies.Departments.departments;
import static net.andreinc.mockneat.unit.id.UUIDs.uuids;
import static net.andreinc.mockneat.unit.networking.URLs.urls;
import static net.andreinc.mockneat.unit.text.Markovs.markovs;
import static net.andreinc.mockneat.unit.text.Strings.strings;
import static net.andreinc.mockneat.unit.text.Words.words;
import static net.andreinc.mockneat.unit.types.Doubles.doubles;
import static net.andreinc.mockneat.unit.types.Ints.ints;
import static net.andreinc.mockneat.unit.user.Names.names;

import de.staatsbibliothek.berlin.hsp.domainmodel.entities.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.andreinc.mockneat.MockNeat;
import net.andreinc.mockneat.utils.file.FileManager;

/**
 * RandomUtils.
 */
public class RandomMessage {
  private final FileManager fm = FileManager.getInstance();
  private LocalDate localDate;
  private String summary;
  private String actorId;
  private Double actorType;
  private String actorName;
  private String actorUrl;
  private String objectId;
  private String objectName;
  private String objectUrl;
  private String targetId;
  private String targetType;
  private String targetName;

  public RandomMessage() {
    MockNeat mock = MockNeat.threadLocal();
    this.localDate = mock.localDates().val();
    this.summary = strings().get();
    this.actorId = uuids().get();
    this.actorType = doubles().range(2000.0, 10000.0).get();
    this.actorName = names().last().get() + ", " + names().first().get();
    this.actorUrl = strings().get();
    this.objectId = uuids().get();
    this.objectName = words().get();
    this.objectUrl = strings().get();
    this.targetId = uuids().get();
    this.targetType = strings().get();
    this.targetName = strings().get();
  }

  public ActivityStream buildRandomActivityStreamMessage() {
    final ActivityStream stream = new ActivityStream();
    stream.setContext("https://www.w3.org/ns/activitystreams");
    stream.setId(this.objectId);
    stream.setSummary(this.summary);
    stream.setPublished(buildNow());
    stream.setType("Create");
    final ActivityStream.Actor actor = new ActivityStream.Actor();
    actor.setId(this.actorId);
    actor.setType(this.actorType);
    actor.setName(this.actorName);
    actor.setUrl(this.actorUrl);
    stream.setActor(actor);
    final ActivityStream.Object object = new ActivityStream.Object();
    object.setId(this.objectId);
    object.setType("KulturObjektDokument");
    object.setName(this.objectName);
    object.setContent(buildDoc());
    object.setUrl(this.objectUrl);
    stream.setObject(object);
    final ActivityStream.Target target = new ActivityStream.Target();
    target.setId(this.targetId);
    target.setName(this.targetName);
    target.setType(this.targetType);
    stream.setTarget(target);
    return stream;
  }

  private String buildNow() {
    final LocalDateTime date = LocalDateTime.now();
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    return date.format(formatter);
  }

  private String buildGnd() {
    return "https://d-nb.info/gnd/" + ints().range(10000, 1000000000).get();
  }

  public KulturObjektDokument buildDoc() {
    final String kodID = UUID.randomUUID().toString();
    final String gndID = buildGnd();
    final List<KulturObjektDokument> subKod = new ArrayList<>();
    final Ort aufbewahrungOrt = new Ort(
        abs(ints().get()), abs(ints().get()), buildGnd(), cities().capitalsEurope().get());
    final List<EntstehungReferenz> enstehungsdaten = new ArrayList<>();
    final EntstehungReferenz entstehungReferenz = new EntstehungReferenz();
    enstehungsdaten.add(entstehungReferenz);
    final List<Beteiligte> besitzendeInstitutionen = new ArrayList<>();
    final Koerperschaft koerperschaft = new Koerperschaft(
        abs(ints().get()), buildGnd(), departments().get());
    final Person person = new Person();
    final Beteiligte beteiligte = new Beteiligte(
        abs(ints().get()), abs(ints().get()), koerperschaft, person);
    besitzendeInstitutionen.add(beteiligte);
    final String signatur = "SIGNATUR " + abs(ints().get());
    final List<Digitalisat> digitalisatKod = new ArrayList<>();
    final List<Volltext> volltextList = new ArrayList<>();
    final Volltext volltext = new Volltext(
        "volltextId", markovs().size(1024).type(LOREM_IPSUM).get(), "inhalt");
    volltextList.add(volltext);
    final List<Image> imageList = new ArrayList<>();
    final Image bilder = new Image(
        "https://example.image-server.de/image/" + abs(ints().get()), "datepfad", "imageName",
        urls().get());
    imageList.add(bilder);
    final Digitalisat digitalisat = new Digitalisat("digital" + ints().range(10000, 100000).get(),
        "beschreibung",
        volltextList, imageList);
    digitalisatKod.add(digitalisat);
    final List<AttributsReferenz> attributRef = new ArrayList<>();
    final List<Person> urheberList = new ArrayList<>();
    final Person urheber = new Person(
        abs(ints().get()), buildGnd(), names().first().get(), names().last().get());
    urheberList.add(urheber);
    final List<Person> authorList = new ArrayList<>();
    final Person author = new Person(
        abs(ints().get()), buildGnd(), names().first().get(), names().last().get());
    authorList.add(author);
    final Beschreibungsdokumenttyp typ = new Beschreibungsdokumenttyp("beschreibung:typ");
    final List<Beteiligte> vorbesitzerList = new ArrayList<>();
    final Beteiligte vorbesitzer = new Beteiligte();
    vorbesitzerList.add(vorbesitzer);
    final List<Beteiligte> herstellerList = new ArrayList<>();
    final Beteiligte hersteller = new Beteiligte();
    herstellerList.add(hersteller);
    final List<Entstehung> entstehungsDatenList = new ArrayList<>();
    final Entstehung entstehungsDaten = new Entstehung();
    entstehungsDatenList.add(entstehungsDaten);
    final List<Koerperschaft> koerperschaften = new ArrayList<>();
    final Koerperschaft koerperschaft1 = new Koerperschaft(
        abs(ints().get()), buildGnd(), departments().get());
    koerperschaften.add(koerperschaft1);
    final Katalog digitalisatKatalog = new Katalog();
    final List<Formtyp> formtypen = new ArrayList<>();
    final Formtyp formtyp = new Formtyp(
        abs(ints().get()), abs(ints().get()), "formTyp1", "a form type description");
    formtypen.add(formtyp);
    final List<Status> statuses = new ArrayList<>();
    final Status status = new Status("status: " + words().get(), "status bsb: " + words().get());
    statuses.add(status);
    final List<Format> formats = new ArrayList<>();
    final Format format = new Format(
        "format: " + words().get(), "format:typ " + words().get(), "format bsb: " + words().get());
    formats.add(format);
    final List<Stoff> stoffe = new ArrayList<>();
    final Stoff stoff = new Stoff();
    stoffe.add(stoff);
    final List<Schreibsprache> schreibsprachen = new ArrayList<>();
    final Schreibsprache schreibsprache = new Schreibsprache(
        abs(ints().get()), abs(ints().get()), words().get(), words().get(), words().get());
    schreibsprachen.add(schreibsprache);
    final List<Schrift> schriften = new ArrayList<>();
    final Schrift schrift = new Schrift(
        abs(ints().get()), abs(ints().get()), words().get(), words().get(), words().get());
    schriften.add(schrift);
    final Beschreibungsdokument referenzID = new Beschreibungsdokument(
        null,
        LocalDateTime.of(this.localDate, LocalTime.of(0, 0)),
        urheberList,
        authorList,
        typ,
        aufbewahrungOrt,
        vorbesitzerList,
        herstellerList,
        entstehungsDatenList,
        koerperschaften,
        digitalisatKatalog,
        formtypen,
        statuses,
        formats,
        stoffe,
        markovs().size(64).type(KAFKA).get(),
        markovs().size(1024).type(KAFKA).get(),
        abs(ints().get()),
        abs(ints().get()),
        abs(ints().get()),
        abs(ints().get()),
        schreibsprachen,
        schriften,
        null);
    final Annotation annotation = new Annotation();
    final AttributsReferenz attributsReferenz = new AttributsReferenz(abs(ints().get()),
        "attribute:typ",
        referenzID, annotation, "wert");
    attributRef.add(attributsReferenz);
    return new KulturObjektDokument(
        kodID, gndID, subKod, aufbewahrungOrt, enstehungsdaten, besitzendeInstitutionen, signatur,
        digitalisatKod, attributRef);
  }
}
