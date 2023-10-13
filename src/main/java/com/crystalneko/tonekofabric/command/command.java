package com.crystalneko.tonekofabric.command;

import com.crystalneko.tonekofabric.libs.base;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static net.minecraft.server.command.CommandManager.*;

public class command{
    private final String worldName;
    public command(){
        //获取世界名称
        worldName = "world";
        //注册命令
        initCommand();
    }

     //--------------------------------------------------------注册命令---------------------------------------------------

    public void initCommand(){

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {


            //------------------------------------------------toneko-----------------------------------------------
            dispatcher.register(literal("toneko")
                    //----------------------------------------player-------------------------------------
                    .then(literal("player")
                            .then(argument("target", StringArgumentType.string())
                                    .suggests(getOnlinePlayers)  //获取玩家列表
                                    .executes(context -> {
                                        final ServerCommandSource source = context.getSource();
                                        // 使用 getArgument 方法获取玩家名称
                                        String target = context.getArgument("target", String.class);
                                        //判断是否有主人
                                        if(base.isNekoHasOwner(target,worldName) == null){
                                            //设置玩家为猫娘
                                            base.setPlayerNeko(target,worldName,source.getName());
                                            //发送成功消息
                                            source.sendMessage(Text.of("§a成功将玩家§6" + target + "§a设置成一个猫娘，你成为了它的主人"));
                                        } else {
                                            source.sendMessage(Text.of("§b它已经是一个猫娘了，它的主人是§6" + base.isNekoHasOwner(target,worldName)));
                                        }

                                        return 1;
                                    })))
                    //----------------------------------------无参数-----------------------------------------
                    .executes(context -> {
                        context.getSource().sendMessage(Text.of("§b/toneko 帮助:\n§a/toneko help §b获取帮助\n§a/toneko player <玩家名> §b将一位玩家变成猫娘(但是你会被祭献)"));
                        return 1;
                    }));
        });
    }
    //-------------------------------------------------------获取在线玩家----------------------------------------------
    private static final SuggestionProvider<ServerCommandSource> getOnlinePlayers = (context, builder) -> {
        for (ServerPlayerEntity player : context.getSource().getServer().getPlayerManager().getPlayerList()) {
            String playerTabList = player.getName().toString();
            //替换字符
            String output = playerTabList.replace("literal{", "").replace("}", "");
            builder.suggest(output);
        }
        return builder.buildFuture();
    };


}

