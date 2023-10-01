package com.github.enderadam;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.activity.ActivityAssets;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.invite.Invite;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;
import org.javacord.api.interaction.callback.InteractionOriginalResponseUpdater;

import javax.swing.Timer;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    private static final List<SlashCommand> allCommands = new ArrayList<>();
    private static final Map<Timer, String> mutedList = new HashMap<>();
    private static boolean isKicking = false;
    private static boolean sendImages = false;
    private static boolean kickPerson = false;
    private static boolean league = true;
    private static final List<Server> servers = new ArrayList<>();
    private static final HashMap<String, KnownCustomEmoji> allEmoji = new HashMap<>();
    private static final Gson gson = new Gson();
    private static List<String> last30Messages = new ArrayList<>();

    private static List<String> quotes;

    private static final String[] gnMessages = {

    };

    public static ArrayList<String> yodayoModels = new ArrayList<>(List.of("stable-diffusion-anime", "holo-waifu", "pastel", "abyss-diffusion", "niji", "sd-anime-classic", "kribo", "konosuba", "bocchi", "ojiberry"));


    public static void main(String[] args) {
        String token = System.getenv("TOKEN");
        String database_url = System.getenv("DATABASE_URL");


        DiscordApi api = new DiscordApiBuilder().setAllIntents().setToken(token).login().join();
        System.out.println("api set");
        Server XXXX = api.getServerById("819262892506611732").isPresent() ? api.getServerById("819262892506611732").get() : null;
        Server ARA = api.getServerById("442268458072276992").isPresent() ? api.getServerById("442268458072276992").get() : null;
        //for deleting
//        List<SlashCommand> commands = api.getServerSlashCommands(XXXX).join();
//        for (SlashCommand i :commands){
//            i.deleteForServer(XXXX);
//        }

        try {
            if (System.getProperty("os.name").toLowerCase().contains("window")) {
                quotes = Files.readAllLines(Paths.get("src/main/resources/quotes.txt"), StandardCharsets.UTF_8);
            } else {
                quotes = Files.readAllLines(Paths.get("./quotes.txt"), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Add a listener which answers with "Pong!" if someone writes "!ping"
        api.addMessageCreateListener(event -> {
            System.out.println(event.getMessage().getContent());
            Message message = event.getMessage();
            String username = event.getMessageAuthor().getName();

//            try {
//                Class.forName("com.mysql.cj.jdbc.Driver");
//                Connection con = DriverManager
//                        .getConnection(database_url);
//                PreparedStatement statement = con.prepareStatement("CREATE TABLE IF NOT EXISTS " + message.getAuthor().getName());
//                statement.executeUpdate();
//                PreparedStatement statement1 = con.prepareStatement()
//            } catch (ClassNotFoundException | SQLException e) {
//                throw new RuntimeException(e);
//            }

//            try {
//                PersonWords[] wordArray = gson.fromJson(new JsonReader(new FileReader("src/main/resources/words.json")), PersonWords[].class);
//                ArrayList<PersonWords> people = new ArrayList<>();
//                if (people.stream().map(x -> x.name).noneMatch(x -> x.equals(username))) {
//                    people.add(new PersonWords(username, new ArrayList<>()));
//                }
//                for (String word : message.getContent().split(" ")) {
//                    int indexOfUsername = people.indexOf(new PersonWords(username));
//                    if (!people.get(indexOfUsername).words.contains(new Words(word))) {
//                        people.get(indexOfUsername).words.add(new Words(word, 1));
//                    } else {
//                        int wordIndex = people.get(indexOfUsername).words.indexOf(new Words(word));
//                        people.get(indexOfUsername).words.get(wordIndex).addOne();
//                    }
//                }
//                if (message.getContent().contains("GIVEMEWORDS")) {
//                    List<String> result =
//                            people.stream().map(x -> x.words).flatMap(x -> x.stream().map(y -> y.count + "")).collect(Collectors.toList());
//                    result.forEach(event.getChannel()::sendMessage);
//                }
//            } catch (FileNotFoundException e) {
//                throw new RuntimeException(e);
//            }

            //Toggles
            toggles(api, XXXX, ARA, message);

            for (KnownCustomEmoji emoji : api.getCustomEmojis()) {
                if (allEmoji.containsKey(emoji.getName())) {
                    allEmoji.put(emoji.getName() + "2", emoji);
                } else {
                    allEmoji.put(emoji.getName(), emoji);
                }
            }
            if (message.getContent().toLowerCase(Locale.ROOT).contains("eclipse")) {
                message.addReaction("\uD83C\uDDF8");
                message.addReaction("\uD83C\uDDED");
                message.addReaction("\uD83C\uDDEE");
                message.addReaction("\uD83C\uDDF9");

            }
            if (league) {
                if ((message.getContent().toLowerCase(Locale.ROOT).contains("league") || (message.getContent().toLowerCase(Locale.ROOT).contains("tft")))
                        && !message.getAuthor().asUser().get().getIdAsString().equalsIgnoreCase("898438764907606066")) {
                    Collection<User> usersInServer = message.getServer().get().getMembers();
                    List<User> leaguers = new ArrayList<>();
                    for (User u : usersInServer) {
                        Set<Activity> activities = u.getActivities();
                        for (Activity a : activities) {
                            if (!a.equals(Optional.empty())) {
                                a.getDetails();
                                if (a.getName().toLowerCase(Locale.ROOT).contains("league of legends")) {
                                    leaguers.add(u);
                                }
                            }
                        }
                    }
                    List<User> tempLeaguers = new ArrayList<>();


                    //check if there are other players in the same vc as those who are playing league
                    if (!leaguers.isEmpty()) {
                        for (User u : leaguers) {
                            if (!u.getConnectedVoiceChannels().stream().toList().isEmpty()) { //if voice channel is not empty
                                System.out.println(u.getConnectedVoiceChannels().stream().toList().get(0).getName());
                                for (ServerVoiceChannel vc : u.getConnectedVoiceChannels()) {
                                    System.out.println(vc.getName());
                                    for (User user : vc.getConnectedUsers()) {
                                        System.out.println(user.getName());
                                        if (!leaguers.contains(user)) {
                                            tempLeaguers.add(user);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //remove duplicates from templeaguers
                    tempLeaguers = tempLeaguers.stream().distinct().collect(Collectors.toList());
                    leaguers.addAll(tempLeaguers);


                    if (!leaguers.isEmpty()) {
                        StringBuilder leaguersToPrint = new StringBuilder();
                        leaguersToPrint.append("These losers are currently playing League:\n");
                        for (User u : leaguers) {
                            if (tempLeaguers.contains(u)) {
                                leaguersToPrint.append(u.getMentionTag() + "(Potentially)").append("\t");
                            } else {
                                leaguersToPrint.append(u.getMentionTag()).append("\t");
                            }
                            for (Activity a : u.getActivities()) {
                                if (a.getName().toLowerCase(Locale.ROOT).contains("league")) {
                                    leaguersToPrint.append(getActivityInfo(a.getAssets())).append("\t");
                                    leaguersToPrint.append(a.getDetails().isPresent() ? a.getDetails().get() : "").append("\t");
                                    // add how long they have been playing for in minutes:seconds
                                    if (a.getStartTime().isPresent()) {
                                        long time = a.getStartTime().get().until(Instant.now(), ChronoUnit.SECONDS);
                                        long minutes = time / 60;
                                        long seconds = time % 60;
                                        if (seconds < 10) {
                                            leaguersToPrint.append(minutes).append(":0").append(seconds).append("\t");
                                        } else {
                                            leaguersToPrint.append(minutes).append(":").append(seconds).append("\t");
                                        }
                                    }
                                }
                            }
                            leaguersToPrint.append("\n");
                        }
                        if (leaguers.size() >= 3) {
                            leaguersToPrint.append("Combo!! We have ").append(leaguers.size()).append(" losers");
                        }
                        message.getChannel().sendMessage(leaguersToPrint.toString()).thenAccept(message1 -> {
                            // wait 10 seconds with a timer
                            api.getThreadPool().getScheduler().schedule(() -> {
                                // delete the message
                                message1.delete();
                            }, 1, TimeUnit.HOURS);
                            message1.addReaction(allEmoji.get("noleague"));
                        });
                    }
                }
            }
            if (message.getContent().toLowerCase().contains("genshin")) {
                // get channel id, send a "GENSHIT" message there and then delete it after 10 seconds
                message.getChannel().sendMessage("genshit*").thenAccept(message1 -> {
                    // wait 10 seconds with a timer
                    api.getThreadPool().getScheduler().schedule(() -> {
                        // delete the message
                        message1.delete();
                    }, 10, TimeUnit.SECONDS);
                });
            }
            if (message.getContent().toLowerCase().contains("joud")) {
                // get channel id, send a "GENSHIT" message there and then delete it after 10 seconds
                message.getChannel().sendMessage("roud*").thenAccept(message1 -> {
                    // wait 10 seconds with a timer
                    api.getThreadPool().getScheduler().schedule(() -> {
                        // delete the message
                        message1.delete();
                    }, 10, TimeUnit.SECONDS);
                });
            }

            if (message.getAuthor().asUser().isPresent()) {
                last30Messages.add(message.getAuthor().asUser().get().getMentionTag() + ": " + message.getContent() + message.getAttachments().stream().map(MessageAttachment::getUrl).toList());
            }
            if (last30Messages.size() > 30) {
                last30Messages.remove(0);
            }


//            if (message.getServer().get().getName().equals("ARA")) {
//                message.addReaction("🤣");
//                message.addReaction("😅");
//                message.addReaction("😂");
//            }


            if ((message.getContent().toLowerCase().contains("google it") ||
                    message.getContent().toLowerCase().contains("just google")) &&
                    !message.getAuthor().asUser().get().getIdAsString().equalsIgnoreCase("898438764907606066")) {
                // get channel id, send a "GENSHIT" message there and then delete it after 10 seconds
                message.getChannel().sendMessage("Google it, simple. Delete this and fuck off. NOW.").thenAccept(message1 -> {
                    // wait 10 seconds with a timer
                    api.getThreadPool().getScheduler().schedule(() -> {
                        // delete the message
                        message1.delete();
                    }, 10, TimeUnit.MINUTES);
                });
            }

            if (!message.getContent().contains(":")) {
//                HashSet<KnownCustomEmoji> allEmoji = new HashSet<>(api.getCustomEmojis());
//                for (KnownCustomEmoji em : allEmoji) {
//                    if (message.getContent().contains(em.getName())) {
//                        message.addReaction(em); //add react
//                        break;
//                    }
//                }

//                giveMeJoeBot(message, sendImages);


//                Pattern forbiddenWords = Pattern.compile("monkey|monky|monke|monkie|m*nkey|monk*y|vibe|joebot|nezuko");
                if (message.getContent().toLowerCase().contains("monkey") || message.getContent().toLowerCase().contains("monky") ||
                        message.getContent().toLowerCase().contains("monke") || message.getContent().toLowerCase().contains("monkie") ||
                        message.getContent().toLowerCase().contains("m*nkey") || message.getContent().toLowerCase().contains("monk*y") ||
                        message.getContent().toLowerCase().contains("vibe") ||
                        message.getContent().toLowerCase().contains("nezuko") || message.getContent().toLowerCase().contains("m0nkey")

                ) {
//                    message.addReaction("🐒"); //monkey
                    kickPerson(api, message, event);
                }

            }
//            if (kickPerson) {
//                if (message.getAuthor().asUser().get().getName().equals("Teyeph")) {
//                    kickPerson(api,message,event);
//                }
//            }
            // if message pings the bot, including everyone, here and roles
            if (message.getMentionedUsers().contains(api.getYourself()) ||
                    message.mentionsEveryone()) {
                event.getChannel().sendMessage("https://tenor.com/view/morbius-ping-morbius-morbiussweep-ping-jared-leto-gif-25020117")
                        .thenAccept(message1 -> {
                            api.getThreadPool().getScheduler().schedule(() -> {
                                // delete the message
                                message1.delete();
                            }, 3, TimeUnit.SECONDS);
                        });
            }
            if (message.getContent().toLowerCase().contains("k-pop") || message.getContent().toLowerCase().contains("kpop")) {
                message.addReaction(allEmoji.get("NoKPop"));
            }
            // if message starts with ion
            if (Arrays.stream(message.getContent().toLowerCase().split(" ")).anyMatch(x -> x.startsWith("ion"))) {
                message.addReaction("⚛");
            }
            if (Arrays.stream(message.getContent().toLowerCase().split(" ")).anyMatch(x -> x.startsWith("forgor"))) {
                message.addReaction("💀");
            }
            if (Arrays.asList(message.getContent().toLowerCase().split(" ")).contains("clearly")) {
                message.addReaction(allEmoji.get("clearly"));
            } else if (message.getContent().toLowerCase().contains("skill issue")) {
                message.addReaction(allEmoji.get("clearly"));
            }
            if (message.getContent().contains("!say") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
                message.delete();
                String messageRead = message.getContent();
                messageRead = messageRead.substring(messageRead.indexOf("!say") + 5);
                StringBuilder toSend = new StringBuilder();
                List<String> emojis = new ArrayList<>();
                while (messageRead.length() > 0) {
//                    if (messageRead.charAt(0)==':'){
//                        messageRead = messageRead.substring(1);
//                        StringBuilder emojiToSend = new StringBuilder();
//                        while (messageRead.charAt(0)!=':'){
//                            emojiToSend.append(messageRead.charAt(0));
//                            messageRead = messageRead.substring(1);
//                        }
//                        messageRead = messageRead.substring(1);
//                        toSend.append(allEmoji.get(emojiToSend.toString()).getMentionTag());
//                    } else {
                    toSend.append(messageRead.charAt(0));
                    messageRead = messageRead.substring(1);
//                    }
                }
                event.getChannel().sendMessage(toSend.toString());
            } else if (message.getContent().contains("!react") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
                String messageRead = message.getContent();
                String[] contents = messageRead.split(" ");
                CompletableFuture<Message> temp = api.getMessageById(contents[1], api.getTextChannelById(contents[2]).get());
                if (allEmoji.containsKey(contents[3])) {
                    temp.thenApply(x -> x.addReaction(allEmoji.get(contents[3])));
                } else {
                    temp.thenApply(x -> x.addReaction(contents[3]));
                }
            } else if (message.getContent().contains("!unreact") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
                String messageRead = message.getContent();
                String[] contents = messageRead.split(" ");
                CompletableFuture<Message> temp = api.getMessageById(contents[1], api.getTextChannelById(contents[2]).get());
                if (allEmoji.containsKey(contents[3])) {
                    temp.thenApply(x -> x.removeReactionByEmoji(allEmoji.get(contents[3])));
                } else {
                    temp.thenApply(x -> x.removeReactionByEmoji(contents[3]));
                }
            } else if (message.getContent().contains("!kickVC") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
                String messageRead = message.getContent();
                String[] contents = messageRead.split(" ");
                api.getUserById(contents[1]).thenApply(x -> x.move(new ArrayList<>(x.getConnectedVoiceChannels()).get(0).getServer()
                        .getVoiceChannels().get(Integer.parseInt(contents[2]))));
            }
            if (message.getContent().toLowerCase().contains("joe")) {
                double random = Math.random();
                if (random < 0.05) {
                    event.getChannel().sendMessage("https://tenor.com/view/axanar-alecpeters-axamonitor-monkey-ape-gif-18121300").thenAccept(message1 -> {
                        api.getThreadPool().getScheduler().schedule(() -> {
                            // delete the message
                            message1.delete();
                        }, 10, TimeUnit.SECONDS);
                    });
                } else if (random < 0.20) {
                    event.getChannel().sendMessage("https://tenor.com/view/kanye-joe-stare-gif-19284974").thenAccept(message1 -> {
                        api.getThreadPool().getScheduler().schedule(() -> {
                            // delete the message
                            message1.delete();
                        }, 10, TimeUnit.SECONDS);
                    });
                } else if (random < 0.51) {
                    event.getChannel().sendMessage("https://tenor.com/view/hey-joe-monkey-monkey-joe-monkey-heart-love-joe-gif-23020196").thenAccept(message1 -> {
                        api.getThreadPool().getScheduler().schedule(() -> {
                            // delete the message
                            message1.delete();
                        }, 10, TimeUnit.SECONDS);
                    });
                }
            }
            if (message.getContent().toLowerCase().contains("ratio")) {
                //if not itself
                if (!message.getAuthor().asUser().get().getIdAsString().equals("898438764907606066")) {
                    double random = Math.random();
                    // send gif than delete it
                    if (random < 0.75) {
                        event.getChannel().sendMessage("https://tenor.com/view/yakuza-ratio-denied-gif-22244085").thenAccept(message1 -> {
                            api.getThreadPool().getScheduler().schedule(() -> {
                                // delete the message
                                message1.delete();
                            }, 10, TimeUnit.SECONDS);
                        });
                    }
                }
            }
            if (message.getContent().toLowerCase().contains("ratio denied") || message.getContent().toLowerCase().contains("ratio-denied")) {
                double random = Math.random();
                if (random < 0.05) {
                    event.getChannel().sendMessage("https://tenor.com/view/ratio-denied-ratio-denied-denied-you-fell-off-bozo-gif-24795104").thenAccept(
                            message1 -> {
                                api.getThreadPool().getScheduler().schedule(() -> {
                                    // delete the message
                                    message1.delete();
                                }, 10, TimeUnit.SECONDS);
                            });
                }
            }
            if (message.getContent().contains("🐒") || message.getContent().contains("🐵")) {
                kickPerson(api, message, event);
            }


//            message.addReactionAddListener(eventReaction -> {
//                if (eventReaction.getEmoji().equalsEmoji("🍕")) { //pizza
//                    eventReaction.deleteMessage();
//                }
//                System.out.println(eventReaction.getEmoji().getMentionTag());
//
//                if (eventReaction.getEmoji().equalsEmoji(allEmoji.get("isleep")) || eventReaction.getEmoji().equalsEmoji(allEmoji.get("isleep2"))) {
//                    if (eventReaction.getUserIdAsString().equalsIgnoreCase("202206936601460736")) {
//                        eventReaction.removeReaction();
//                        eventReaction.addReactionsToMessage(allEmoji.get("nosleep"));
//                    }
//                }
//                if (eventReaction.getEmoji().equalsEmoji("🐒")) {
//                    eventReaction.getUser().get().sendMessage("SPAM");
//                    if (isKicking) {
//                        if (!eventReaction.getUserIdAsString().equalsIgnoreCase("246637425961467904")) {
//                            message.getServer().get().kickUser(api.getUserById(eventReaction.getUserIdAsString()).join());
//                        }
//                    }
//
//                }
//            }).removeAfter(30, TimeUnit.MINUTES);
        });


        SlashCommand pingCommand = SlashCommand.with("ping", "Replies with Pong!")
//                .createForServer(XXXX)
                .createGlobal(api)
                .join();
        allCommands.add(pingCommand);

        SlashCommand quoteCommand = SlashCommand.with("quote", "Gets a Joe quote")
                .createGlobal(api)
                .join();
        allCommands.add(quoteCommand);
        SlashCommand allQuotesCommand = SlashCommand.with("allquotes", "Gets all Joe quotes")
                .createGlobal(api)
                .join();
        allCommands.add(allQuotesCommand);

        SlashCommand gnCommand = SlashCommand.with("gn", "Says GoodNight")
                .createGlobal(api)
                .join();
        allCommands.add(gnCommand);

        SlashCommand actionsCommand = SlashCommand.with("actions", "Shows all possible actions with JoeBot")
                .createGlobal(api)
                .join();
        allCommands.add(actionsCommand);
        SlashCommand emotesCommand = SlashCommand.with("emotes", "Shows all emotes of the server")
                .createGlobal(api)
                .join();
        allCommands.add(emotesCommand);
        SlashCommand emoteCommand = SlashCommand.with("emote", "Make JoeBot send an emote",
                        Arrays.asList(SlashCommandOption.createWithOptions(SlashCommandOptionType.STRING, "emoji", "emoji to send")))
                .createGlobal(api)
                .join();
        allCommands.add(emoteCommand);

        SlashCommand yodayoCommand = SlashCommand.with("yodayo", "Query Yodayo with a provided query and model (both options required)",
                        Arrays.asList(SlashCommandOption.createWithOptions(SlashCommandOptionType.STRING, "query", "query to search"),
                                SlashCommandOption.createWithOptions(SlashCommandOptionType.DECIMAL, "model", "model to use (1-10)"),
                                SlashCommandOption.createWithOptions(SlashCommandOptionType.STRING, "xcsrf", "xcsrf from yodayo"),
                                SlashCommandOption.createWithOptions(SlashCommandOptionType.STRING, "cookie", "cookie from yodayo")))
                .createGlobal(api)
                .join();
        allCommands.add(yodayoCommand);

        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            if (slashCommandInteraction.getCommandName().equals("ping")) {
                ping(slashCommandInteraction);
            } else if (slashCommandInteraction.getCommandName().equals("quote")) {
                quote(slashCommandInteraction);
            } else if (slashCommandInteraction.getCommandName().equals("actions")) {
                actionsList(slashCommandInteraction, api);
            } else if (slashCommandInteraction.getCommandName().equals("gn")) {
                gnMethod(slashCommandInteraction);
            } else if (slashCommandInteraction.getCommandName().equals("emotes")) {
                emotesCommand(slashCommandInteraction);
            } else if (slashCommandInteraction.getCommandName().equals("emote")) {
                emoteCommand(slashCommandInteraction);
            } else if (slashCommandInteraction.getCommandName().equals("allquotes")) {
                allQuotesCommand(slashCommandInteraction);
            } else if (slashCommandInteraction.getCommandName().equals("yodayo")) {
                yodayoCommand(slashCommandInteraction);
            }

        });


        // Print the invite url of your bot
        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite(Permissions.fromBitmask(8)));
        api.getUserById("246637425961467904").thenApply(x -> x.sendMessage("Restarted Bot"));
    }

    private static void toggles(DiscordApi api, Server XXXX, Server ARA, Message message) {
        if (message.getContent().equals("!kicking") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
            isKicking = !isKicking;
            message.getAuthor().asUser().get().sendMessage("Kicking is " + isKicking);
        }
        if (message.getContent().equals("!sendImages") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
            sendImages = !sendImages;
            message.getAuthor().asUser().get().sendMessage("Sending Images is " + sendImages);
        }
        if (message.getContent().equals("!kickPerson") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
            kickPerson = !kickPerson;
            message.getAuthor().asUser().get().sendMessage("Kicking Jamie is " + kickPerson);
        }
        if (message.getContent().equals("!league") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
            league = !league;
            message.getAuthor().asUser().get().sendMessage("League option is " + league);
        }
        if (message.getContent().contains("!changeNick") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
            String[] parts = message.getContent().split(" ");
            StringBuilder concatNick = new StringBuilder();
            Server toChange = null;
            if (parts[2].equals("ARA")) {
                toChange = ARA;
            } else {
                toChange = XXXX;
            }
            for (int i = 3; i < parts.length; i++) {
                concatNick.append(" ").append(parts[i]);
            }
            changeNick(api.getUserById(parts[1]).join(), api, toChange, concatNick.toString());
        }
        if (message.getContent().contains("!messages") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
            String toSend = String.join("\n", last30Messages);
            message.getAuthor().asUser().get().sendMessage("Messages option is: \n" + toSend);
        }
        if (message.getContent().contains("!getMessages") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
            String messageRead = message.getContent();
            String[] contents = messageRead.split(" ");
            String channelID = contents[1];

            //find the channel with the given id
            //export all the messages as html file
            //send the file to the user
            TextChannel channel = api.getTextChannelById(channelID).orElse(null);
            if (channel != null) {
                channel.getMessagesAsStream().forEach(message1 -> {
                    try {
                        FileWriter myWriter = new FileWriter("messages.txt", true);
                        //add author name, message content, message time and attachments
                        myWriter.write(message1.getAuthor().getName() + " " + message1.getContent() + " " + message1.getCreationTimestamp() + " " + message1.getAttachments() + "\n");
                        myWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }


        }
        if (message.getContent().contains("!servers") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
//                message.getAuthor().asUser().get().sendMessage(ARA.getInvites().join().toString());
            StringBuilder sb = new StringBuilder();
            servers.clear();
            for (Server server : api.getServers()) {
                sb.append(server.getName()).append("\n");
                servers.add(server);
                try {
                    for (Invite invite : server.getInvites().join()) {
                        sb.append(invite.getUrl()).append("\n");
                    }
                } catch (java.util.concurrent.CompletionException ignored) {
                }
            }
            message.getAuthor().asUser().get().sendMessage(sb.toString());
        }
        if (message.getContent().contains("!listServer") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
//                message.getAuthor().asUser().get().sendMessage(ARA.getInvites().join().toString());
            StringBuilder sb = new StringBuilder();
            Server serverToLook = servers.get(Integer.parseInt((message.getContent().split("!listServer")[1].substring(1))));
            for (User u : serverToLook.getMembers()) {
                sb.append(u.getName() + "\n");
            }
            message.getAuthor().asUser().get().sendMessage(sb.toString());
        }
        if (message.getContent().contains("!activity") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
            api.updateActivity((message.getContent().split("!activity")[1]));
        } else if (message.getContent().contains("!unsetactivity") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
            api.unsetActivity();
        }
    }

    private static void giveMeJoeBot(Message message, boolean b) {
        if (!b) {
            return;
        }

        List<String> allImages = new ArrayList<>();
        try {
            allImages = FileUtils.getAllImages(new File(""), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> ganbareImages = new ArrayList<>();
        try {
            ganbareImages = FileUtils.getAllImages(new File(""), false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!allImages.isEmpty()) {
            if (message.getContent().toLowerCase().contains("give me mommy milkers") ||
                    message.getContent().toLowerCase().contains("give me <@!" + 898438764907606066L + ">")) {
                String[] loop = message.getContent().split(">");
                if (loop.length >= 2) {
                    int loopint = 1;
                    try {
                        loopint = Integer.parseInt(loop[1].substring(1));
                        if (loopint > 20) {
                            loopint = 20;
                        }
                    } catch (NumberFormatException ignored) {

                    }
                    for (int i = 0; i < loopint; i++) {
                        message.getAuthor().asUser().get().sendMessage((new File(allImages.get((int) ((Math.random()) * allImages.size())))));
                    }
                } else {
                    message.getAuthor().asUser().get().sendMessage((new File(allImages.get((int) ((Math.random()) * allImages.size())))));
                }
            }
        }
        if (!ganbareImages.isEmpty()) {
            if (message.getContent().toLowerCase().contains("give me ganbare")) {
                message.getChannel().sendMessage((new File(ganbareImages.get((int) ((Math.random()) * ganbareImages.size())))));
            }
        }
    }

    private static void kickPerson(DiscordApi api, Message message, MessageCreateEvent event) {

        if (!isKicking) {
            return;
        }
//        message.getAuthor().asUser().get().sendMessage("SPAM");
        if (message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
            System.out.println(message.getServer().get().canKickUser(api.getYourself(), message.getAuthor().asUser().get()));
        } else {
            System.out.println(message.getServer().get());
            System.out.println(message.getAuthor().asUser().get());
            boolean canKickUser = message.getServer().get().canKickUser(api.getYourself(), message.getAuthor().asUser().get());
            System.out.println(canKickUser);
            message.getServer().get().kickUser(message.getUserAuthor().get(), "Hate Speech");
            if (canKickUser)
                event.getChannel().sendMessage(message.getUserAuthor().get().getName() + " is kicked 😤");
//            javax.swing.Timer muted = new javax.swing.Timer(1000 * 10, null);
//            muted.addActionListener(e -> {
//                message.getServer().get().unmuteUser(message.getUserAuthor().get());
//                muted.stop();
//            });
//            muted.start();
        }
    }

    private static void ping(SlashCommandInteraction slashCommandInteraction) {
        slashCommandInteraction.createImmediateResponder()
                .setContent("Pong!")
                .respond();
    }

    private static void quote(SlashCommandInteraction slashCommandInteraction) {
        slashCommandInteraction.createImmediateResponder()
                .setContent(quotes.get((int) (Math.random() * quotes.size())))
                .respond();
    }

    private static void allQuotesCommand(SlashCommandInteraction slashCommandInteraction) {
        StringBuilder temp = new StringBuilder();
        for (String s : quotes) {
            temp.append(s).append("\n");
        }
        slashCommandInteraction.createImmediateResponder()
                .setContent(temp.toString())
                .respond();
    }

    private static void yodayoCommand(SlashCommandInteraction slashCommandInteraction) {
        // Couldn't figure out how to make options required for javacord so this is a workaround
        if (slashCommandInteraction.getArguments().size() != 4 || slashCommandInteraction.getArguments().get(1).getDecimalValue().isEmpty()) {
            slashCommandInteraction.createImmediateResponder()
                    .setContent("Both arguments are required")
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
            return;
        }

        var prompt = slashCommandInteraction.getArguments().get(0).getStringValue().get();
        var model = slashCommandInteraction.getArguments().get(1).getDecimalValue().get();
        var modelString = yodayoModels.get((int) (model - 1));
        var xcsrf = slashCommandInteraction.getArguments().get(2).getStringValue().get();
        var cookie = slashCommandInteraction.getArguments().get(3).getStringValue().get();

        // Send the request to the site and respond to the user
        slashCommandInteraction.respondLater().thenAccept(interaction -> {
            try {
                requestYodayoImage(prompt, modelString, interaction, slashCommandInteraction.getChannel().get(), xcsrf, cookie, slashCommandInteraction.getUser());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static String generateUUID() {
        //generate a random UUID in this format "https://api.yodayo.com/v1/text_to_images/5fee5d73-2986-41c8-9f54-e19ef6891c50"
        StringBuilder uuid = new StringBuilder().append("https://api.yodayo.com/v1/text_to_images/");
        UUID temp = UUID.randomUUID();
        uuid.append(temp);
        System.out.println(uuid.toString());
        return uuid.toString();
    }

    private static void requestYodayoImage(String prompt, String modelString, InteractionOriginalResponseUpdater interaction, TextChannel channel, String xcsrf, String cookie, User user) throws IOException {
        // Request the image from the site and wait for it to be generated before sending to the user


//        URL myURL = new URL(generateUUID());
//        HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();
//        myURLConnection.setRequestMethod("POST");
//        myURLConnection.setRequestProperty("header", "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/109.0");
//        myURLConnection.setRequestProperty("header", "Accept: application/json, text/plain, */*");
//        myURLConnection.setRequestProperty("header", "Accept-Language: en-US,en;q=0.5");
//        myURLConnection.setRequestProperty("header", "X-CSRF-Token:" + xcsrf);
//        myURLConnection.setRequestProperty("header", "Content-Type: application/json");
//        myURLConnection.setRequestProperty("header", "Origin: https://yodayo.com");
//        myURLConnection.setRequestProperty("header", "Connection: keep-alive");
//        myURLConnection.setRequestProperty("header", "Referer: https://yodayo.com/");
//        myURLConnection.setRequestProperty("header", "Cookie:" + cookie);
//        myURLConnection.setRequestProperty("header", "Sec-Fetch-Dest: empty");
//        myURLConnection.setRequestProperty("header", "Sec-Fetch-Mode: cors");
//        myURLConnection.setRequestProperty("header", "Sec-Fetch-Site: same-origin");
//        myURLConnection.setRequestProperty("header", "Sec-GPC: 1");
//        myURLConnection.setRequestProperty("header", "TE: Trailers");
//        myURLConnection.setRequestProperty("header", "Pragma: no-cache");
//        myURLConnection.setRequestProperty("header", "Cache-Control: no-cache");
//        myURLConnection.setRequestProperty("header", "Accept-Encoding: gzip, deflate, br");
//        myURLConnection.setRequestProperty("header", "Content-Length: 0");
//        myURLConnection.setDoOutput(true);
//        myURLConnection.setDoInput(true);
//        myURLConnection.setRequestProperty("body", "{\"prompt\":\"" + prompt + "\",\"model\":\"" + modelString + "\"\",\"\"sampling_steps\"\":100,\"\"sampling_method\"\":\"\"k_euler_ancestral\"\",\"\"cfg_scale\"\":10,\"\"height\"\":768,\"\"width\"\":512,\"\"seed\"\":-1,\"\"priority\"\":\"\"low\"\"}");
//        myURLConnection.connect(); // Or something like that
//
//        System.out.println(myURLConnection.getResponseMessage());
//        System.out.println(myURLConnection.getResponseCode());
//        System.out.println(myURLConnection.getContentType());
//        System.out.println(myURLConnection.getContentLength());
//        System.out.println(myURLConnection.getContentEncoding());
//        System.out.println(myURLConnection.getContent());
//        System.out.println(myURLConnection.getInputStream());
//        var json = gson.fromJson(new InputStreamReader(myURLConnection.getInputStream()), JsonObject.class);
//        System.out.println(json);
//        String uuid = json.get("uuid").getAsString();

//        await fetch("https://api.yodayo.com/v1/text_to_images/e6330713-3550-4c37-a93e-cd4aa54ce633", {
//                "credentials": "include",
//                "headers": {
//            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/110.0",
//                    "Accept": "application/json, text/plain, */*",
//                    "Accept-Language": "en-US,en;q=0.5",
//                    "X-CSRF-Token": "QjpzlN+Se9jUNdvKN1FeZypnvaTbrtPYEqRk751eAOB0ISyuJ1PfNheTS2910Gvpa/Q8r9YZg+x7k61I/8Gqiw==",
//                    "Content-Type": "application/json",
//                    "Sec-Fetch-Dest": "empty",
//                    "Sec-Fetch-Mode": "cors",
//                    "Sec-Fetch-Site": "same-site"
//        },
//        "referrer": "https://yodayo.com/",
//                "body": "{\"prompt\":\"hakos baelz, cute, smiling, cute grown up, adult\",\"negative_prompt\":\"(bad_prompt:0.8),  lowres, text, error, cropped, worst quality, low quality, jpeg artifacts, ugly, duplicate, morbid, mutilated, out of frame, extra fingers, mutated hands, poorly drawn hands, poorly drawn face, mutation, deformed, blurry, dehydrated, bad anatomy, bad proportions, extra limbs, cloned face, disfigured, gross proportions, malformed limbs, missing arms, missing legs, extra arms, extra legs, fused fingers, too many fingers, long neck, username, watermark, signature,(((deformed))), [blurry], (poorly drawn hands)\",\"model\":\"holo-waifu\",\"sampling_steps\":100,\"sampling_method\":\"k_euler_ancestral\",\"cfg_scale\":10,\"height\":768,\"width\":512,\"seed\":2029546840,\"priority\":\"low\"}",
//                "method": "POST",
//                "mode": "cors"
//});
        String command;
        if (System.getProperty("os.name").toLowerCase().contains("window")) {
            command =
                    "curl \"" + generateUUID() + "\" -X POST -H \"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/109.0\" -H \"Accept: application/json, text/plain, */*\" -H \"Accept-Language: en-US,en;q=0.5\" -H \"X-CSRF-Token:" + xcsrf + "\" -H \"Content-Type: application/json\" -H \"Origin: https://yodayo.com\" -H \"Connection: keep-alive\" -H \"Referer: https://yodayo.com/\" -H \"Cookie:" + cookie + "\" -H \"Sec-Fetch-Dest: empty\" -H \"Sec-Fetch-Mode: cors\" -H \"Sec-Fetch-Site: same-site\" -H \"TE: trailers\" --data-raw \"{\"\"prompt\"\":\"\"" + prompt + "\"\",\"\"negative_prompt\"\":\"\"(bad_prompt:0.8),  lowres, text, error, cropped, worst quality, low quality, jpeg artifacts, ugly, duplicate, morbid, mutilated, out of frame, extra fingers, mutated hands, poorly drawn hands, poorly drawn face, mutation, deformed, blurry, dehydrated, bad anatomy, bad proportions, extra limbs, cloned face, disfigured, gross proportions, malformed limbs, missing arms, missing legs, extra arms, extra legs, fused fingers, too many fingers, long neck, username, watermark, signature,(((deformed))), ^[blurry^], (poorly drawn hands)\"\",\"\"model\"\":\"\"" + modelString + "\"\",\"\"sampling_steps\"\":100,\"\"sampling_method\"\":\"\"k_euler_ancestral\"\",\"\"cfg_scale\"\":10,\"\"height\"\":768,\"\"width\"\":512,\"\"seed\"\":-1,\"\"priority\"\":\"\"low\"\"}\"";
        } else {
            command =
                    "curl " + generateUUID() + " -X POST -H User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/109.0 -H Accept: application/json, text/plain, */* -H Accept-Language: en-US,en;q=0.5 -H X-CSRF-Token:" + xcsrf + " -H Content-Type: application/json -H Origin: https://yodayo.com -H Connection: keep-alive -H Referer: https://yodayo.com/ -H Cookie:" + cookie + " -H Sec-Fetch-Dest: empty -H Sec-Fetch-Mode: cors -H Sec-Fetch-Site: same-site -H TE: trailers --data-raw {\"prompt\":\"" + prompt + "\",\"negative_prompt\":\"(bad_prompt:0.8), lowres, text, error, cropped, worst quality, low quality, jpeg artifacts, ugly, duplicate, morbid, mutilated, out of frame, extra fingers, mutated hands, poorly drawn hands, poorly drawn face, mutation, deformed, blurry, dehydrated, bad anatomy, bad proportions, extra limbs, cloned face, disfigured, gross proportions, malformed limbs, missing arms, missing legs, extra arms, extra legs, fused fingers, too many fingers, long neck, username, watermark, signature,(((deformed))), ^[blurry^], (poorly drawn hands)\",\"model\":\"" + modelString + "\",\"sampling_steps\":100,\"sampling_method\":\"k_euler_ancestral\",\"cfg_scale\":10,\"height\":768,\"width\":512,\"seed\":-1,\"priority\":\"low\"}";
        }

        System.out.println(command);

        String uuid = "";
        try {
            Process process = Runtime.getRuntime().exec(command);
            //print out the output of the command
            //convert inputstream of process to string


            var json = gson.fromJson(new InputStreamReader(process.getInputStream()), JsonObject.class);
            uuid = json.get("uuid").getAsString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }


        interaction.setFlags(MessageFlag.EPHEMERAL);
        interaction.setContent("Request sent");
        interaction.update();

        // Create a new completeableFuture to wait 25 minutes then download and send the image to the channel
        String finalUuid = uuid;
        CompletableFuture.runAsync(() -> getYodayoImage(finalUuid, interaction, channel, cookie, user, prompt));
    }

    private static void getYodayoImage(String finalUuid, InteractionOriginalResponseUpdater interaction, TextChannel channel, String cookie, User user, String prompt) {
        // Wait for 5 minutes before trying due to the API being slow
        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Attempt to download the image from the server and then send it to the channel
        try {
            String getCommand;
            if (System.getProperty("os.name").toLowerCase().contains("window")) {
                getCommand = "curl \"https://api.yodayo.com/v1/text_to_images?offset=0&limit=25\" -H \"User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/109.0\" -H \"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\" -H \"Accept-Language: en-US,en;q=0.5\" -H \"Connection: keep-alive\" -H \"Cookie:" + cookie + "\" -H \"Upgrade-Insecure-Requests: 1\" -H \"Sec-Fetch-Dest: document\" -H \"Sec-Fetch-Mode: navigate\" -H \"Sec-Fetch-Site: none\" -H \"Sec-Fetch-User: ?1\"";
            } else {
                getCommand = "curl https://api.yodayo.com/v1/text_to_images?offset=0&limit=25 -H User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/109.0 -H Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8 -H Accept-Language: en-US,en;q=0.5 -H Connection: keep-alive -H Cookie:" + cookie + " -H Upgrade-Insecure-Requests: 1 -H Sec-Fetch-Dest: document -H Sec-Fetch-Mode: navigate -H Sec-Fetch-Site: none -H Sec-Fetch-User: ?1";
            }


            Process p = Runtime.getRuntime().exec(getCommand);
            var json = gson.fromJson(new InputStreamReader(p.getInputStream()), JsonObject.class);
            for (JsonElement e : json.get("text_to_images").getAsJsonArray()) {
                var uuid2 = e.getAsJsonObject().get("uuid").getAsString();
                if (uuid2.equals(finalUuid)) {
                    if (e.getAsJsonObject().get("state").getAsString().equals("failed")) {
                        interaction.setContent("Image Generation Failed, try again");
                        interaction.update();
                        return;
                    }

                    var url = e.getAsJsonObject().get("output_image_url").getAsString();
                    if (url.equals("")) {
                        // Try again if it's not ready yet
                        getYodayoImage(finalUuid, interaction, channel, cookie, user, prompt);
                        return;
                    }
                    var file = new File("SPOILER_" + uuid2 + ".png");

                    // Save to the file
                    BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    byte dataBuffer[] = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                    }

                    // Delete initial message
//                    interaction.delete();
//                    interaction.update();
                    String message = "Prompt: " + prompt + "\n" +
                            "Requested by: " + user.getMentionTag() + "\n";

                    channel.sendMessage(message, file).join();

                    // Delete the file after finishing with it
                    fileOutputStream.close();
                    in.close();
                    file.delete();
                    return;
                }
            }

        } catch (IOException ignored) {
        }
    }

    private static void gnMethod(SlashCommandInteraction slashCommandInteraction) {
        slashCommandInteraction.createImmediateResponder()
                .setContent(gnMessages[(int) (Math.random() * gnMessages.length)])
                .respond();
    }

    private static void emotesCommand(SlashCommandInteraction slashCommandInteraction) {

        StringBuilder result = new StringBuilder();
        for (Emoji e : slashCommandInteraction.getServer().get().getCustomEmojis()) {
            result.append(e.getMentionTag()).append(" ");
        }
        slashCommandInteraction.createImmediateResponder()
                .setContent(result.toString())
                .respond();
    }

    private static void emoteCommand(SlashCommandInteraction slashCommandInteraction) {

        StringBuilder result = new StringBuilder();
        String name = slashCommandInteraction.getArguments().get(0).toString();
        result.append(slashCommandInteraction.getServer().get().getCustomEmojis().stream().filter(x -> x.getName().equals(name)).collect(Collectors.toList()));
        slashCommandInteraction.createImmediateResponder()
                .setContent(result.toString())
                .respond();
    }

    private static void actionsList(SlashCommandInteraction slashCommandInteraction, DiscordApi api) {
        slashCommandInteraction.createImmediateResponder()
                .setContent(calculateActions())
                .respond();
    }

    private static String calculateActions() {
        StringBuilder result = new StringBuilder();
        for (SlashCommand com : allCommands) {
            result.append(com.getName()).append("\t\t\t").append(com.getDescription()).append("\n");
        }
        return result.toString();
    }

    private static void changeNick(User user, DiscordApi api, Server server, String newNick) {
//       api.getYourself().updateNickname(server,newNick);
        user.updateNickname(server, newNick);
    }

    private static String getActivityInfo(Optional<ActivityAssets> opt) {
        if (!opt.isPresent()) {
            return "";
        }
        ActivityAssets a = opt.get();
        StringBuilder result = new StringBuilder();
//        a.getLargeImage().ifPresent(x-> result.append(x.getUrl().toString()));
//        a.getSmallImage().ifPresent(x-> result.append(x.getUrl().toString()));
        a.getLargeText().ifPresent(result::append);
        a.getSmallText().ifPresent(result::append);
        return result.toString();
    }
}
