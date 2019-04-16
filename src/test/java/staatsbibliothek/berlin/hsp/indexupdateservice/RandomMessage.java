package staatsbibliothek.berlin.hsp.indexupdateservice;

import static net.andreinc.mockneat.unit.address.Countries.countries;
import static net.andreinc.mockneat.unit.id.UUIDs.uuids;
import static net.andreinc.mockneat.unit.text.Strings.strings;
import static net.andreinc.mockneat.unit.types.Doubles.doubles;
import static net.andreinc.mockneat.unit.user.Names.names;

import de.staatsbibliothek.berlin.hsp.domainmodel.entities.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    this.objectName = countries().names().get();
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
    stream.setPublished(this.localDate.toString());
    stream.setType("Create");
    final ActivityStream.Actor actor = new ActivityStream.Actor();
    actor.setId(this.actorId);
    actor.setType(this.actorType);
    actor.setName(this.actorName);
    actor.setUrl(this.actorUrl);
    stream.setActor(actor);
    final ActivityStream.Object object = new ActivityStream.Object();
    object.setId(this.objectId);
    object.setType("Article");
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

  public KulturObjektDokument buildDoc() {
    final String kodID = UUID.randomUUID().toString();
    final String gndID = "gnd123456";
    final List<KulturObjektDokument> subKod = new ArrayList<>();
    final Ort aufbewahrungOrt = new Ort(103, 102, "gnd103", "Ort103");
    final List<EntstehungReferenz> enstehungsdaten = new ArrayList<>();
    final EntstehungReferenz entstehungReferenz = new EntstehungReferenz();
    enstehungsdaten.add(entstehungReferenz);
    final List<Beteiligte> besitzendeInstitutionen = new ArrayList<>();
    final Koerperschaft koerperschaft = new Koerperschaft(101, "gnd101", "Koeperschaft101");
    final Person person = new Person();
    final Beteiligte beteiligte = new Beteiligte(1001, 12, koerperschaft, person);
    besitzendeInstitutionen.add(beteiligte);
    final String signatur = "SIGNATUR 12345";
    final List<Digitalisat> digitalisatKod = new ArrayList<>();
    final List<Volltext> volltextList = new ArrayList<>();
    final Volltext volltext = new Volltext("volltextId", "beschreibung", "inhalt");
    volltextList.add(volltext);
    final List<Image> imageList = new ArrayList<>();
    final Image bilder = new Image("imageId", "datepfad", "imageName", "imageUrl");
    imageList.add(bilder);
    final Digitalisat digitalisat = new Digitalisat("digital001", "beschreibung",
        volltextList, imageList);
    digitalisatKod.add(digitalisat);
    final List<AttributsReferenz> attributRef = new ArrayList<>();
    final LocalDateTime date = LocalDateTime.now();
    final List<Person> urheberList = new ArrayList<>();
    final Person urheber = new Person(103, "gnd123456", "UrheberVorname", "UrheberNachname");
    urheberList.add(urheber);
    final List<Person> authorList = new ArrayList<>();
    final Person author = new Person(104, "gnd123457", "AuthorVorname", "AuthorNachname");
    authorList.add(author);
    final Beschreibungsdokumenttyp typ = new Beschreibungsdokumenttyp("beschreibungsdokumenttyp");
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
    final Koerperschaft koerperschaft1 = new Koerperschaft(102, "gnd102", "Koeperschaft102");
    koerperschaften.add(koerperschaft1);
    final Katalog digitalisatKatalog = new Katalog();
    final List<Formtyp> formtypen = new ArrayList<>();
    final Formtyp formtyp = new Formtyp(10001, 10002, "formTyp1", "a form type description");
    formtypen.add(formtyp);
    final List<Status> statuses = new ArrayList<>();
    final Status status = new Status();
    statuses.add(status);
    final List<Format> formats = new ArrayList<>();
    final Format format = new Format();
    formats.add(format);
    final List<Stoff> stoffe = new ArrayList<>();
    final Stoff stoff = new Stoff();
    stoffe.add(stoff);
    final List<Schreibsprache> schreibsprachen = new ArrayList<>();
    final Schreibsprache schreibsprache = new Schreibsprache(
        4000, 40003, "Fraktur", "a Type", "description of Fraktur");
    schreibsprachen.add(schreibsprache);
    final List<Schrift> schriften = new ArrayList<>();
    final Schrift schrift = new Schrift();
    schriften.add(schrift);
    final Beschreibungsdokument referenzID = new Beschreibungsdokument(
        null,
        date,
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
        "titel",
        "beschreibungsFreitext",
        0,
        0,
        10,
        11,
        schreibsprachen,
        schriften,
        null);
    final Annotation annotation = new Annotation();
    final AttributsReferenz attributsReferenz = new AttributsReferenz(1, "attributeType",
        referenzID, annotation, "wert");
    attributRef.add(attributsReferenz);
    return new KulturObjektDokument(
        kodID, gndID, subKod, aufbewahrungOrt, enstehungsdaten, besitzendeInstitutionen, signatur,
        digitalisatKod, attributRef);
  }
}
