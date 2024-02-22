package net.indevo.do_or_die_thirst;

import net.indevo.do_or_die_thirst.api.ThirstHelper;
import net.indevo.do_or_die_thirst.content.registry.ItemInit;
import net.indevo.do_or_die_thirst.content.thirst.PlayerThirst;
import net.indevo.do_or_die_thirst.foundation.common.capability.IThirst;
import net.indevo.do_or_die_thirst.foundation.config.ClientConfig;
import net.indevo.do_or_die_thirst.foundation.config.CommonConfig;
import net.indevo.do_or_die_thirst.foundation.config.ItemSettingsConfig;
import net.indevo.do_or_die_thirst.foundation.config.KeyWordConfig;
import net.indevo.do_or_die_thirst.foundation.gui.ThirstBarRenderer;
import net.indevo.do_or_die_thirst.foundation.gui.appleskin.HUDOverlayHandler;
import net.indevo.do_or_die_thirst.foundation.gui.appleskin.TooltipOverlayHandler;
import net.indevo.do_or_die_thirst.foundation.network.ThirstModPacketHandler;
import net.indevo.do_or_die_thirst.content.purity.WaterPurity;
import net.indevo.do_or_die_thirst.foundation.tab.ThirstTab;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Do_Or_Die_Thirst.ID)
public class Do_Or_Die_Thirst
{
    public static final String ID = "thirst";

    public Do_Or_Die_Thirst()
    {

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::commonSetup);
        modBus.addListener(this::clientSetup);
        modBus.addListener(this::registerCapabilities);

        if(FMLEnvironment.dist.isClient()){
            modBus.addListener(ThirstBarRenderer::registerThirstOverlay);

            if(ModList.get().isLoaded("appleskin"))
            {
                HUDOverlayHandler.init();
                TooltipOverlayHandler.init();
                modBus.addListener(this::onRegisterClientTooltipComponentFactories);
            }
        }

        ItemInit.ITEMS.register(modBus);

        ThirstTab.register(modBus);

        //configs
        ItemSettingsConfig.setup();
        CommonConfig.setup();
        ClientConfig.setup();
        KeyWordConfig.setup();
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        WaterPurity.init();
        ThirstModPacketHandler.init();

        if(ModList.get().isLoaded("coldsweat"))
            ThirstHelper.shouldUseColdSweatCaps(true);

        if(ModList.get().isLoaded("tombstone"))
            PlayerThirst.checkTombstoneEffects = true;

        if(ModList.get().isLoaded("vampirism"))
            PlayerThirst.checkVampirismEffects = true;

        if(ModList.get().isLoaded("farmersdelight"))
            PlayerThirst.checkFDEffects = true;

        if(ModList.get().isLoaded("bakery"))
            PlayerThirst.checkLetsDoBakeryEffects = true;
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        if(ModList.get().isLoaded("vampirism"))
        {
            ThirstBarRenderer.checkIfPlayerIsVampire = true;
        }
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        event.register(IThirst.class);
    }

    //this is from Create but it looked very cool
    public static ResourceLocation asResource(String path)
    {
        return new ResourceLocation(ID, path);
    }
    private void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        TooltipOverlayHandler.register(event);
    }
}
