package com.github.enderadam;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

import javax.swing.Timer;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

//import org.json.simple.*;

public class Main {
    private static List<SlashCommand> allCommands = new ArrayList<>();
    private static Map<Timer, String> mutedList = new HashMap<>();

    private static String[] quotes = {"If you have a problem figuring out whether you’re for me or Trump, then you ain’t black.",
            "I may be Irish but I’m not stupid.",
            "Poor kids are just as bright and just as talented as white kids.",
            "Now we have over 120 million dead from COVID.",
            "I shouldn’t have been such a wise guy.",
            "Eat some chocolate chocolate chip",
            "I got hairy legs that turn blonde in the Sun. Kids used to come up and reach into the pool and rub my leg down",
            "I learned about roaches, I learned about kids jumping on my lap. And I love kids jumping on my lap."
    };

    public static void main(String[] args) {
        // Insert your bot's token here
        String token = "ODk4NDM4NzY0OTA3NjA2MDY2.YWkOTQ.b3eZUH6JYq56wYaJGlQ9sUSM8mA";

        //google
        //https://www.google.com/search?q=the&tbm=isch


        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
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

//            if (message.getContent().equalsIgnoreCase("!joe")){
//                System.out.println("test");
//                try {
//                    String google = "http://www.google.com/search?q=";
//                    String search = "Joe Biden";
//                    String charset = "UTF-8";
//                    String userAgent = "ExampleBot 1.0 (+http://example.com/bot)"; // Change this to your company's name and bot homepage!
//
//                    Elements links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get().select(".g>.r>a");
//                    System.out.println(links.size());
//                    for (Element link : links) {
//                        String title = link.text();
//                        String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
//                        url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");
//                        System.out.println("test3");
//
//                        System.out.println("Title: " + title);
//                        System.out.println("URL: " + url);
//                    }
//                } catch (IOException e){
//                    System.out.println(e.toString());
//                }
//            }
            HashMap<String, KnownCustomEmoji> allEmoji = new HashMap<>();
            for (KnownCustomEmoji emoji : api.getCustomEmojis()) {
                if (allEmoji.containsKey(emoji.getName())) {
                    allEmoji.put(emoji.getName() + "2", emoji);
                } else {
                    allEmoji.put(emoji.getName(), emoji);
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

                giveMeJoBot(message, false);


//                Pattern forbiddenWords = Pattern.compile("monkey|monky|monke|monkie|m*nkey|monk*y|vibe|joebot|nezuko");
                if (message.getContent().toLowerCase().contains("monkey") || message.getContent().toLowerCase().contains("monky") ||
                        message.getContent().toLowerCase().contains("monke") || message.getContent().toLowerCase().contains("monkie") ||
                        message.getContent().toLowerCase().contains("m*nkey") || message.getContent().toLowerCase().contains("monk*y") ||
                        message.getContent().toLowerCase().contains("vibe") || message.getContent().toLowerCase().contains("joebot") ||
                        message.getContent().toLowerCase().contains("nezuko") || message.getContent().toLowerCase().contains("m0nkey")

                ) {
//                    message.addReaction("🐒"); //monkey
                    kickPerson(api, message, event);
                }
                if (message.getContent().toLowerCase().contains("nigga-chan") || message.getContent().toLowerCase().contains("niggachan")) {
                    message.addReaction("🥰");
                }
                if (Arrays.asList(message.getContent().toLowerCase().split(" ")).contains("ion")) {
                    message.addReaction("⚛️");
                }
                if (Arrays.asList(message.getContent().toLowerCase().split(" ")).contains("forgor")) {
                    message.addReaction("💀");
                }
            }
            if (message.getContent().contains("@2")) {
                message.delete();
                event.getChannel().sendMessage((message.getContent().split("@2")[1]));
            }
            if (message.getContent().contains("joe")) {
                if (Math.random() < 0.1)
                    event.getChannel().sendMessage("https://tenor.com/view/hey-joe-monkey-monkey-joe-monkey-heart-love-joe-gif-23020196");
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
                if (eventReaction.getEmoji().equalsEmoji("🐒")) {
                    eventReaction.getUser().get().sendMessage("SPAM");
//                    if (!eventReaction.getUserIdAsString().equalsIgnoreCase("246637425961467904")) {
//                        message.getServer().get().kickUser(eventReaction.getUser().get(), "Hate Speech");
//                    }
                }
            }).removeAfter(30, TimeUnit.MINUTES);
        });


        /**
         * Slash Commands to add to Discord
         */
        SlashCommand pingCommand = SlashCommand.with("ping", "Replies with Pong!")
//                .createForServer(XXXX)
                .createGlobal(api)
                .join();
        allCommands.add(pingCommand);

        SlashCommand quoteCommand = SlashCommand.with("quote", "Gets a Joe quote")
                .createGlobal(api)
                .join();
        allCommands.add(quoteCommand);

        SlashCommand actionsCommand = SlashCommand.with("actions", "Shows all possible actions with JoBot")
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

    private static void giveMeJoBot(Message message, boolean b) {
        if (!b) {
            return;
        }

        List<String> allImages = new ArrayList<>();
        try {
            allImages = FileUtils.getAllImages(new File("D:\\Torrent Files\\WeebSpoiler"), false);
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
    }

    private static void kickPerson(DiscordApi api, Message message, MessageCreateEvent event) {

//        message.getAuthor().asUser().get().sendMessage("SPAM");
        if (message.getAuthor().asUser().get().getName().equals("EnderAdam")) {
            System.out.println(message.getServer().get().canKickUser(api.getYourself(), message.getAuthor().asUser().get()));
        } else {
            System.out.println(message.getServer().get());
            System.out.println(message.getAuthor().asUser().get());
            System.out.println(message.getServer().get().canMuteMembers(message.getAuthor().asUser().get()));
            message.getServer().get().kickUser(message.getUserAuthor().get(), "Hate Speech");
            event.getChannel().sendMessage(message.getUserAuthor().get().getName() + "is kicked 😤");
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

}