package com.hugosama.samalinne.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hugosama.samalinne.data.SamalinneContract.MessagesEntry;

import java.util.Calendar;

/**
 * Created by hgomez on 11/02/2016.
 */
public class SamalinneDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "samalinne.db";

    public SamalinneDbHelper(Context context ) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MESSAGES_TABLE = " CREATE TABLE " + MessagesEntry.TABLE_NAME + "(" +
                MessagesEntry._ID + " INTEGER PRIMARY KEY , " +
                MessagesEntry.COLUMN_MESSAGE + " TEXT UNIQUE NOT NULL , " +
                MessagesEntry.COLUMN_DATE + " INTEGER NOT NULL " +
                ");";
        String [] messagesArray = new String[] {
                "Trabajar duro es importante, pero tomarte un buen descanso lo es también. "
                ,"Cuando estés aburrida triste o en problemas. Recuerda que la puerta de la imaginación siempre está abierta para llevarte al lugar de tus sueños. "
                ," No te arrepientas de tus errores, aprende de ellos y sé mejor cada dia. No erra, solo aquel que no vive. "
                ," La vida es un conjunto de sorpresas….  porque nadie puede predecir el futuro. Disfruta cada una de ellas y compártelas (conmigo de preferencia ). "
                ,"Nunca hay prisa suficiente como para arriesgarse a un accidente, Mejor tarde pero segura. "
                ," Busca personas que no te repriman y que te den consejos. Aléjate de aquellos que buscan dañar a los demás con sus palabras… "
                ," Cuando te sientas atrapada, sal afuera, respira profundo. Hoy es un buen dia… "
                ," Lo bonito de la vida es que siempre pasan cosas que no esperas. No todas son buenas, pero es divertido ver las coincidencias … "
                ," Haz sentir felicidad y no tristeza a tu alrededor. Si necesitas ayuda pídela , no busques salir del precipicio sola, habiendo alguien que te pueda ayudar. "
                ," Cuando sientas felicidad… brinca, salta,grita, baila y contagia al mundo con ella ¡, pocos somos los afortunados que tenemos ese privilegio. "
                ," Si no lo intentas, nunca sabrás si puedes lograrlo. "
                ," No se puede mejorar, si no se intentan cosas nuevas. No tengas miedo. "
                ," Una vida con miedo no es vida. Si quieres algo haz lo necesario por conseguirlo, demuestra que realmente lo deseas. "
                ," Nunca digas nunca. Vivimos en un universo en el que, si lo puedes imaginar, se puede lograr. sigue tus sueños. "
                ," Cosas muy divertidas pasan inesperadamente, pero a veces no es bueno reírse en el momento. prudencia ante todo. "
                ," No hay mayor satisfacción que acompañar a quien mas quieres, cuando más lo necesita. La gente triste aleja a los demás, nunca te vayas. "
                ," Sabes que quieres alguien, cuando te das cuenta q darías la vida sin pensarlo dos veces. "
                ," Hay cosas que no se pueden explicar con palabras, demuéstralas con acciones. "
                ,"It’s impossible to understand everyone ,  get to know yourself and do not base your happiness on others.  "
                ," Life pays patience with big taxes. Never be in a hurry, life is short, but full of happiness for those who learned to wait. "
                ,"Felicidades en tu día. Sin ti damita, seguiría vagando sin sentido. Gracias por darme un camino y hacer que la naranja esté completa. "
                ," Don’t think about how long or exhausting is going to be, get it done! That’s the key to productivity. "
                ," Sometimes all that’s left to say is I love you. "
                ," Nunca te des por vencida.  "
                ," Si algo puede salir mal, saldrá mal. Prepárate siempre para el peor de los casos. "
                ,"Cuando despertamos sabemos que necesitamos 24 horas para vivir un dia mas, que necesitamos razones para continuar, pero 1 de ellas nos hace mejorar: entender que en la vida podemos tropezar y por donde mismo no volvera a pasar. Caer, equivocarnos, nos da fortaleza para asegurar que por mas tropiezos en el camino, de la vida nos podamos enamorar. No estás solo. Tu saltas, yo salto.. "
                ," No hay mejor lugar como el hogar. "
                ,"A veces te la pasas tan bien, que olvidas lo que tenias que hacer. Pero la vida no es para vivir atado a una rutina. Disfruta, pero atente a las consecuencias y nunca te arrepientas. "
                ," sé tu misma. "
                ,"Disfruta el momento, que no te importe nada mas. "
                ,"A veces la vida te pone en la cima un momento y te entierra al otro .Disfruta y sobre llévalos, siempre con una sonrisa en tu rostro. "
                ," Confía en ti misma. Tu todo lo puedes y si no yo te ayudo!. "
                ," Los sueños se hacen realidad. "
                ," Mejorar no es una ciencia exacta. A veces el camino no solo es hacia arriba y hay que retroceder para poder seguir avanzando. "
                ," El fracaso es parte del camino al éxito. Sigue con la mirada firme hacia tu objetivo. "
                ," La convivencia familiar es importante. No hay otro lugar donde sientas tanta confianza. "
                ," Si haces y dices lo que sientes con los que más quieres, no derramarás ni una gota de arrepentimiento. "
                ," La vida es bella ante los ojos de cualquier niño. "
                ," El hábito de la lectura te convierte en una persona asombrosa. "
                ," La palabra de un hombre es lo más importante que tiene, y debe mantenerla sea cual sea la situación, a menos que afecte a los que más quiere. "
                ," Haz hasta lo imposible por demostrar cuanto quieres a alguien. Ver su sonrisa gracias a tu esfuerzo no tiene comparación. "
                ," A veces tienes que tomar decisiones difíciles por el bien de los que más quieres. "
                ," La comunicación es la base del éxito. Siempre cuenta lo que sientes y libera la presión del corazón. "
                ," La belleza de la naturaleza se encuentra en todos lados, admírala. "
                ," Bailar es raro si lo piensas. No lo hagas, solo siéntelo, muévete y disfruta.  "
                ," La espontaneidad hace a la vida impredecible, pero divertida. "
                ,"; Puedes querer a muchas personas, pero realmente solo amas a una toda tu vida. "
                ,"El amor no conoce límites ni fronteras. "
                ," No puedes estar con la persona que amas todo el tiempo, pero un lazo de amor te puede hacer sentir junto a ella.  "
                ,"Valora y atesora cada momento de felicidad. Al final del camino. Todo es un gran recuerdo y qué mejor que lleno de alegría. "
                ," Hay cosas que por más que te esfuerces no puedes controlar. No dejes que te arruinen el día. "
                ," Hay que hacer sacrificios y esforzarse para salir de un abismo y seguir viviendo feliz. "
                ," Nunca hay presión de nada, tómate tu tiempo y haz las cosas bien.  "
                ,"Si amas a alguien no lo sueltes sin luchar. "
                ," Sonríe, ríe, carcajéate, rueda en el piso!,  "
                ," Sé fuerte. Por ti....  "
                ," La comida es una necesidad, pero eso no quiere decir que no se pueda disfrutar. "
                ," Soltarse y disfrutar el momento está bien; no tengas miedo. "
                ," Tomarse el día libre y disfrutar, siempre ayuda a relajar. "
                ," Crea una amistad de confianza. Alguien a quien puedas contarle todo y siempre serás feliz.  "
                ," Toda canción hecha con el corazón es hermosa. "
                ," Las cosas siempre se pueden poner peor. "
                ," Una madre que te conoce bien, te dice lo que necesitas escuchar para abrir los ojos y el corazón. "
                ," Repón todo lo que no pudiste decir o hacer con las personas que mas quieres. "
                ," Siempre búscale el lado bueno a todo lo que pasa y te aseguro que nunca dejaras de ser feliz. "
                ," Las cosas pueden salir mejor de lo que esperas. Disfrútalo. "
                ," No siempre esta en ti salvar a un amigo. "
                ," Nunca sabrás si no lo intentas "
                ," En las buenas y en las malas, yo te apoyare. "
                ," Lo espontáneo, inesperado, sorpresivo saca una sonrisa automática en los demás. "
                ,"Eres única para mi, nadie puede sanar mis heridas tan rápido y sin dejar cicatriz. "
                ,"Si haces lo mismo todos los días, se pierde la magia, A veces es bueno un pequeño descanso. "
                ," Las aventuras son divertidas y agotadoras. "
                ," A veces no necesitas confirmar cuando confías en que te esperan. "
                ," Tarde pero seguro. "
                ,"la naturaleza es excelente calmando el temperamento. "
                ," Cuando te corren de un lugar es mejor irse. "
                ," Nunca dejes de intentarlo. "
                ," Recuerda lo que te digo cada día. "
                ," La vida siempre traerá nuevos problemas, pero es bueno tener alguien que te ayude a resolverlos. "
                ," cuando no te sientas bien tu, aléjate de los demás, no quieres encontrar un desastre cuando vuelvas. "
                ," No todas las batallas se tienen que pelear a golpes o gritos. Busca tu manera de luchar por lo que quieres. "
                ," Tú eres capaz de convertir una mala experiencia, en algo que hace más fuerte tus lazos con las personas.  "
                ," Disfruta el momento. "
                ," aprender y entender este mundo, es lo que venimos a hacer. "
                ," Si tienes ganas de correr, hazlo, el cansancio es mejor que la congoja. "
                ," Los quince años son una fiesta muy importante, hay que disfrutarlos. "
                ," VIVE. "
                ," Todo esfuerzo requiere un descanso para recuperar fuerzas. "
                ," Pasar el rato en el parque con tanta naturaleza, sonrisas y recuerdos bonitos te tranquiliza rápidamente. "
                ," Hay cosas horribles en este mundo que debemos ignorar. En caso de que no podamos hacer nada. "
                ," Nunca vivas para darle gusto a los demás, nunca vas a tener tiempo de darte gusto a ti. "
                ," Vive tus sueños. "
                ," No te vayas sin avisar, sin conversar, no tengas miedos de los errores que puedes cometer, hasta que pase acepta las consecuencias y no te arrepientas. "
                ," No vas a amarrarte, vas con tu alma gemela a conversar y ver que tal le va. A sacarle una sonrisa y verla a los ojos. "
                ," El amor de mi vida eres tú. "
                ," Un hombre debe permanecer calmado en las situaciones mas difíciles. "
                ," Estar afuera hace que te despejes y te relajes. "
                ," Que tu amada llegue a tu lado inesperadamente te hace sentir especial. "
                ," Pocos son los eventos que pasan una vez en la vida. Disfrútalos, son estos los que nos marcan la existencia. "
                ," No importa que tan duro paresca el camino, lucha por lo que està en la cima. "
                ," Cosas inesperadas pueden pasar,disfrutalas! "
                ," Cuando se te olvide el dinero, disfruta la caminata de regreso, hay que ver el lado BUENO de las cosas. "
                ," lo intente. Te amo "
                ," si no es bueno, no es necesario y no ayuda, mejor no contarlo. "
                ," el amor es magico "
                ," primero la salud "
                ,"Si quieres que pase algo diferente, has algo diferente. "
                ,"La vida tiene tantas posibilidades que entre mas conoces, menos sabes que hacer con ellas. "
                ,"Después de equivocarte tanto y que sientas que nada te sale bien, solo te queda seguir luchando por mejorar. "
                ,"Enfocate en los buenos ratos y olvida los malos. "
                ,"Si tu orgullo puede mas que tu amor, es imposible que veas el mundo de color. "
                ,"Cuando amas a alguien realmente, solo piensas en esa persona a cada instante del día. "
                ,"Los callejones son peligrosos, cuidate mucho cuando pases por uno ;) "
                ,"La convivencia familiar es lo mejor que hay para llenar el corazon "
                ,"La suerte siempre nos acompaña, solo hay que saberla utilizar. "
                ,"No todo lo caro es bueno, aprende a identificar. "
                ,"GRACIAS POR ESTAR CONMIGO TODO ESTE TIEMPO. "
                ," el éxito solo se consigue trabajando duro. "
                ," no dejes que lo malo se imponga a lo bueno. "
                ," agarra un ritmo que puedas mantener y sube desde ahí. "
                ,"no vale la pena vivir enojado, disfruta de otros aspectos del mundo mientras pasa. "
                ,"a veces estoy jodido. "
                ,"la primera cosa en la que debes pensar, es en la salud de tu persona especial. "
                ,"nunca dejes las cosas a medias. "
                ,"se el mejor. "
                ," si rompes tus promesas te come el coco. "
                ,"hasta los mejores amigos se pelean. "
                ,"lee todos tus mensajes, puede haber una oportunidad que puedes perder. "
                ,"salvar a una princesa no es tan fácil como en los cuentos de hadas. "
                ," mantente tranquila, aun en la obscuridad y veras como empiezas a disfrutar. "
                ,"al final, todo será un gran recuerdo. "
                ," te soñare. "
                ,"no contagies al mundo de amargura mejor regalale una sonrisa. "
                ," tu pudes ser el héroe principal de cualquier historia, basta con esforzarte cada dia. "
                ,"no dejes de sonreir,la gente a tu alrededor depende de ti. "
                ,"nunca le prometas a tu chica algo que no puedas cumplir. "
                ,"busca la manera de ver lo mas que puedas a la persona que mas quieres- "
                ,"aprovecha la chispa del nuevo dia. "
                ," hay cosas que no puedes controlar, y solo te queda vivir con ellas. "
                ," nunca rendirse, es la clave para ascender en todos los aspectos de esta vida, ponerse obstáculos solo hace tu camino mas dificil "
                ,"nuevo dia, nuevas posibilidades. "
                ,"sin esfuerzo y dedicación no hay recompensa. "
                ,"vive por aquellos a los que la vida les duro poco. "
                ," los limites solo se superan, exigiéndote al máximo. "
                ,"comparte la suerte que te ha brindado este mundo, con los que mas quieres. "
                ,"no dejes que te tumben del camino. "
                ," no existen días con 25 horas, pero todos deseamos algunos. "
                ,"la humildad es lo mas conmovedor que puedes encontrar. "
                ,"se tiene que poder no pierdas la esperanza jamás. "
                ,"trabajar 24 horas es una experiencia única. "
                ," hoy soñare con lo mas hermoso en el mundo, TU. "
                ," te quiero ver sonreir. "
                ,"daria mi vida, porque tu ya no sufrieras. "
                ," no importa de que estado de animo te encuentres, nunca olvides el cumpleaños de tu padre. "
                ,"no te rindas, no tires la toalla, lucha por tu sueño. "
                ," no es lo que batallas para lograr las cosas, es el resultado lo que importa. "
                ,"aquel que hace las cosas bien, no tiene miedo en ningún lado. "
                ,"siempre hay tiempo para un abrazo "
                ," canta hasta que tu hermosa voz, se escuche a lo largo de este bello mundo. "
                ,"lucha por ganarte su sonrisa cada dia de tu vida y nunca te arrepentiras de nada. "
                ," con buenas herramientas, puedes construir mas y mejores cosas. "
                ," di las cosas de manera que las personas se sientan bien con ellas mismas. "
                ," y a pesar de todo, yo la amo. "
                ," si al te amo le quitas la “a” de aventura…. No es amor. "
                ,"un hombre debe poner la felicidad de los que ama por sobre la suya. "
                ," feliz navidad a ti el regalo mas grande que me ha dado la vida. "
                ," dar las buenas noches es esperar verla a la mañana siguiente. "
                ," descansado, el dia se ve muy diferente. "
                ," resuelve las cosas en el momento. "
                ," se que algún dia entenderas que lo que trato de hacer, es ayudarte a que no te hagan sufrir los demás y puedas vivir tu vida plena. "
                ,"me encanta recordarle al amor de mi vida, que la amo. "
                ,"no lo sabes todo, pedir ayuda no te hace débil necesitas a los demás. "
                ,"todo llega a su fin, disfruta de todo. "
                ," disfruta del amor. "
                ," un abrazo y un te quiero de tu madre, hace que desaparesca cualquier mal. "
                ,"un caballero no debe de perder su objetivo, cuidar a su princesa.. "
                ,"soñar es gratis, vivir y  cumplir los sueños, cuesta esfuerzo. "
                ," yo amo a esa niña miedosa, porque tiene todo para brillar. "
                ,"repasa lo que hiciste en el dia y piensa como lo podrías haber hecho mejor. "
                ,"soy menso con la gente, pero no lo soy tanto como para no buscar mejorar. "
                ,"sueña, no lo olvides. "
                ,"las personas que mas importan, aparecen en tus mas lindos sueños. "
                ,"cuida de tu chica, es frágil y se preocupa mucho. "
                ,"soy un buen hombre para ella. "
                ,"ella, es la persona mas maravillosa que jamás conocere y la hare sonreir en cada oportunidad que tenga. "
                ," disfruta de tus momentos juntos. "
                ,"hoy somos uno. "
                ," la vida no es, las veces que te caes, son las veces que te levantas  y sigues intentándolo. "
                ,"las grandes esperas tienen su recompensa.. "
                ,"que gran dia tuvimos hoy. "
                ," amor del bueno. "
                ,"hacer cosas diferentes, hace que pasen cosas diferentes. "
                ,"los optimistas siempre triunfan, aunque nos llamen locos. "
                ,"disfruta de la vida donde quiera que estes. "
                ," buenas noches es desear soñar bonito y descansar a tu persona especial. "
                ," la vida es bella con mi doncella. "
                ,"la hora de getearse es sagrada. "
                ," un caballero siempre listo para el rescate. "
                ,"el amor es una fuerza indescriptiblemente bella. "
                ,"ser un líder implica hacer las cosas, no imponerlas. "
                ," la familia siempre es primero. "
                ," todo es posible cuando crees en ti. "
                ," si eres bueno sonriendo, eres bueno sacando sonrisas. "
                ," la tentación es la fruta de la pasión. "
                ,"no vivas con miedo a perder, vive con las ganas de ganar. "
                ," mantente positivo e imaginativo, siempre ante cualquier situación. "
                ," toda experiencia es  parte de la vida, vivela. "
                ,"reconoce que necesitas ayuda, no siempre puedes solo. "
                ," un dia como este pero hace un año, conoci al amor de mi vida y todo mi mundo cambio para bien. "
                ," no pierdas de vista que hay cosas que dices porque las sientes y porque también quieres recibirlas. "
                ,"nunca te rindas. "
                ," no importa si los demas no creen en ti, confía en ti. "
                ," nunca dejes de soñar. "
                ," después de dar todo de ti solo queda relajarte. "
                ,"y la verdad es que estoy enamorado. "
                ,"el amor jamas debe ser solo corporal, de lo contrario todo iria fatal. "
                ,"no hagas caso al dicho: la curiosidad mato al gato, gracias a la curiosidad suceden cosas asombrosas "
                ,"ayuda "
                ,"si la tratas bien, pueden pasar 100 años, y ella seguira enamorada de ti "
                ," enamorar una mujer, no es decirle cosas bonitas, es crear momentos de felicidad que jamas olvidara. "
                ,"cuida de la reina de tu corazon "
                ,"sueña con el mundo que deseas. "
                ,"aveces hay que estar solo contigo mismo. "
                ,"si tu chica cocina rico, ya la hiciste  xD "
                ,"estoy seguro de que lo que siento por ti es amor del bueno "
                ,"feliz dia mi cielo, espero me permitas pasar muchos años más junto a ti "
                ,"no propongas nada cuando tienes sueño "
                ,"tira con todo lo que tengas. "
                ,"mira al pasado y hazlo mejor. "
                ,"no te rebajes al nivel de los demás se mejor "
                ,"disfruta sin limites, para eso se vive. "
                ,"descansar es tan importante como vivir "
                ,"eres el sueño que vivo mientras estoy despierto "
                ,"no dejes de intentar contactar a quien amas. "
                ,"me encanta verte "
                ,"manten la frente en alto, alguien te ama "
                ,"solo dejate llevar "
                ,"jamas olvides tus ideales. "
                ,"hay veces que lo sabe el pensamiento, pero no los sentimientos "
                ,"no importa si esta enojado o feliz, no por eso dejara de ser tu amor "
                ,"es lindo cuidar de tu persona especial. "
                ," no te exaltes, disfruta "
                ,"no importa si el cielo esta nublado,nunca dejes de soñar "
                ,"me gustas en 3 tonos musicales a =MI LA DO! "
                ,"mira lo que quieres con claridad y los pasos para lograrlo seran sencillos de realizar "
                ,"si no saldra nada bueno de alguna decision, no la tomes "
                ,"casa, es donde estan pensando constantemente en ti "
        };
        //insertando un mensaje por dia
        String SQL_POPULATE_MESSAGES = "INSERT INTO " + MessagesEntry.TABLE_NAME + "("+
                MessagesEntry.COLUMN_MESSAGE + ","+ MessagesEntry.COLUMN_DATE + ") VALUES ";
        Calendar cal = Calendar.getInstance();
        cal.set(2016,0,1);
        long date = cal.getTimeInMillis();
        long millisInADay = 1000 * 60 * 60 * 24;
        for( String message : messagesArray) {
            SQL_POPULATE_MESSAGES += "(" +
                    "\""+message + "\",\""+ SamalinneContract.normalizeDate(date) + "\"),";
            date += millisInADay;
        }
        SQL_POPULATE_MESSAGES = SQL_POPULATE_MESSAGES.substring(0,SQL_POPULATE_MESSAGES.length() - 1);
        db.execSQL(SQL_CREATE_MESSAGES_TABLE);
        db.execSQL(SQL_POPULATE_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MessagesEntry.TABLE_NAME);
        onCreate(db);
    }
}
