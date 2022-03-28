package com.github.enderadam;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

import javax.swing.Timer;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

//import org.json.simple.*;

public class Main {
    private static final List<SlashCommand> allCommands = new ArrayList<>();
    private static final Map<Timer, String> mutedList = new HashMap<>();
    private static boolean isKicking = false;
    private static boolean sendImages = false;
    private static boolean kickPerson = false;
    private static boolean league = true;

    private static final String[] quotes = {"If you have a problem figuring out whether you’re for me or Trump, then you ain’t black.",
            "I may be Irish but I’m not stupid.",
            "Poor kids are just as bright and just as talented as white kids.",
            "I shouldn’t have been such a wise guy.",
            "Eat some chocolate chocolate chip",
            "I got hairy legs that turn blonde in the Sun. Kids used to come up and reach into the pool and rub my leg down",
            "I learned about roaches, I learned about kids jumping on my lap. And I love kids jumping on my lap.",
            "Now we have over 120 million dead from COVID",
            "I shouldn't have been such a wise guy",
            "How many times did you see people pulling up to McDonald’s, sitting outside during the pandemic so they could do their homework because they couldn’t get — get it off of their — their line?",
            "Successful dump, dropped everything at the dump. It all worked out. And btw, I got a second load guys coming in if anybody wants to help me unload",
            "The next president of the United States; Barack America",
            "I promise you, the president has a big stick. I promise you."

    };

    public static void main(String[] args) {
        // Insert your bot's token here (Hidden)
        String token = System.getenv("TOKEN");

        //google
        //https://www.google.com/search?q=the&tbm=isch


        DiscordApi api = new DiscordApiBuilder().setAllIntents().setToken(token).login().join();
        System.out.println("api set");
        Server XXXX = api.getServerById("819262892506611732").isPresent() ? api.getServerById("819262892506611732").get() : null;
        Server ARA = api.getServerById("442268458072276992").isPresent() ? api.getServerById("442268458072276992").get() : null;
        //for deleting
//        List<SlashCommand> commands = api.getServerSlashCommands(XXXX).join();
//        for (SlashCommand i :commands){
//            i.deleteForServer(XXXX);
//        }

        // Add a listener which answers with "Pong!" if someone writes "!ping"
        api.addMessageCreateListener(event -> {
            Message message = event.getMessage();

            //Toggles
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
            if (message.getContent().contains("!servers") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
//                message.getAuthor().asUser().get().sendMessage(ARA.getInvites().join().toString());
                StringBuilder sb = new StringBuilder();
                for (Server server : api.getServers()) {
                    sb.append(server.getName()).append("\n");
                    try {
                        sb.append(server.getInvites().join()).append("\n");
                    } catch (java.util.concurrent.CompletionException ignored){
                    }
                }
                message.getAuthor().asUser().get().sendMessage(sb.toString());
            }
            if (message.getContent().contains("!activity") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
                api.updateActivity((message.getContent().split("!activity")[1]));
            } else if (message.getContent().contains("!unsetactivity") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
                api.unsetActivity();
            }

            HashMap<String, KnownCustomEmoji> allEmoji = new HashMap<>();
            for (KnownCustomEmoji emoji : api.getCustomEmojis()) {
                if (allEmoji.containsKey(emoji.getName())) {
                    allEmoji.put(emoji.getName() + "2", emoji);
                } else {
                    allEmoji.put(emoji.getName(), emoji);
                }
            }
            if (message.getContent().toLowerCase(Locale.ROOT).contains("eclipse")){
                if (Math.random() < 0.25) {
                    message.addReaction("\uD83C\uDDF8");
                    message.addReaction("\uD83C\uDDED");
                    message.addReaction("\uD83C\uDDEE");
                    message.addReaction("\uD83C\uDDF9");
                }
            }
            if (league) {
                if (message.getContent().toLowerCase(Locale.ROOT).contains("league") && !message.getAuthor().asUser().get().getIdAsString().equalsIgnoreCase("898438764907606066")) {
                    Collection<User> usersInServer = message.getServer().get().getMembers();
                    List<User> leaguers = new ArrayList<>();
                    for (User u : usersInServer) {
                        Set<Activity> activities = u.getActivities();
                        for (Activity a : activities) {
                            if (!a.equals(Optional.empty())) {
                                a.getDetails();
                                if (a.getName().toLowerCase(Locale.ROOT).contains("league")) {
                                    leaguers.add(u);
                                }
                            }
                        }
                    }
                    if (!leaguers.isEmpty()) {
                        StringBuilder leaguersToPrint = new StringBuilder();
                        leaguersToPrint.append("These losers are currently playing League:\n");
                        for (User u : leaguers) {
                            leaguersToPrint.append(u.getMentionTag()).append("\n");
                        }
                        if (leaguers.size() >= 3) {
                            leaguersToPrint.append("Combo!! We have ").append(leaguers.size()).append(" losers");
                        }
                        message.getChannel().sendMessage(leaguersToPrint.toString());
                    }
                }
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
            if (message.getContent().toLowerCase().contains("k-pop") || message.getContent().toLowerCase().contains("kpop")) {
                message.addReaction(allEmoji.get("garbage_trash"));
            }
            if (message.getContent().toLowerCase().contains("nigga-chan") || message.getContent().toLowerCase().contains("niggachan")) {
                message.addReaction("🥰");
            }
            if (Arrays.asList(message.getContent().toLowerCase().split(" ")).contains("ion")) {
                message.addReaction("⚛");
            }
            if (Arrays.asList(message.getContent().toLowerCase().split(" ")).contains("forgor")) {
                message.addReaction("💀");
            }
            if (Arrays.asList(message.getContent().toLowerCase().split(" ")).contains("clearly")) {
                message.addReaction(allEmoji.get("clearly"));
            }
            if (message.getContent().contains("!say") && message.getAuthor().asUser().get().getIdAsString().equals("246637425961467904")) {
                message.delete();
                event.getChannel().sendMessage((message.getContent().split("!say")[1]));
            }
            if (message.getContent().toLowerCase().contains("joe")) {
                if (Math.random() < 0.15) {
                    event.getChannel().sendMessage("https://tenor.com/view/hey-joe-monkey-monkey-joe-monkey-heart-love-joe-gif-23020196");
                    message.getChannel().sendMessage("Responded to: " + message.getAuthor().getName());
                } else if (Math.random() < 0.05){
                    event.getChannel().sendMessage("https://tenor.com/view/axanar-alecpeters-axamonitor-monkey-ape-gif-18121300");
                    message.getChannel().sendMessage("Responded to: " + message.getAuthor().getName());
                }
            }
            if (message.getContent().contains("🐒") || message.getContent().contains("🐵")) {
                kickPerson(api, message, event);
            }


            message.addReactionAddListener(eventReaction -> {
                if (eventReaction.getEmoji().equalsEmoji("🍕")) { //pizza
//                    eventReaction.deleteMessage();
                }
//                System.out.println(eventReaction.getEmoji().getMentionTag());

//                if (eventReaction.getEmoji().equalsEmoji(allEmoji.get("isleep")) || eventReaction.getEmoji().equalsEmoji(allEmoji.get("isleep2"))) {
//                    if (eventReaction.getUserIdAsString().equalsIgnoreCase("202206936601460736")) {
//                        eventReaction.removeReaction();
//                        eventReaction.addReactionsToMessage(allEmoji.get("nosleep"));
//                    }
//                }
//                if (eventReaction.getEmoji().equalsEmoji("🐒")) {
////                    eventReaction.getUser().get().sendMessage("SPAM");
//                    if (isKicking) {
//                        if (!eventReaction.getUserIdAsString().equalsIgnoreCase("246637425961467904")) {
//                            message.getServer().get().kickUser(api.getUserById(eventReaction.getUserIdAsString()).join());
//                        }
//                    }
//
//                }
            }).removeAfter(30, TimeUnit.MINUTES);
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

        SlashCommand actionsCommand = SlashCommand.with("actions", "Shows all possible actions with JoeBot")
                .createGlobal(api)
                .join();
        allCommands.add(actionsCommand);


        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            if (slashCommandInteraction.getCommandName().equals("ping")) {
                ping(slashCommandInteraction);

            } else if (slashCommandInteraction.getCommandName().equals("quote")) {
                quote(slashCommandInteraction);
            } else if (slashCommandInteraction.getCommandName().equals("actions")) {
                actionsList(slashCommandInteraction, api);
            }

        });


        // Print the invite url of your bot
        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
    }

    private static void giveMeJoeBot(Message message, boolean b) {
        if (!b) {
            return;
        }

        List<String> allImages = new ArrayList<>();
        try {
            allImages = FileUtils.getAllImages(new File("D:\\Torrent Files\\WeebSpoiler"), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> ganbareImages = new ArrayList<>();
        try {
            ganbareImages = FileUtils.getAllImages(new File("D:\\Torrent Files\\Ganbare Douki-Chan"), false);
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
                .setContent(quotes[(int) (Math.random() * quotes.length)])
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
            result.append(com.getName()).append("   ").append(com.getDescription()).append("\n");
        }
        return result.toString();
    }

    private static void changeNick(User user, DiscordApi api, Server server, String newNick) {
//       api.getYourself().updateNickname(server,newNick);
        user.updateNickname(server, newNick);
    }

}