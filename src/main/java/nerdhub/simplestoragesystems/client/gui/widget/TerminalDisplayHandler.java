package nerdhub.simplestoragesystems.client.gui.widget;

import com.google.common.collect.Lists;
import nerdhub.simplestoragesystems.api.ISimpleItemStack;
import nerdhub.simplestoragesystems.client.gui.gui.GuiTerminal;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TerminalDisplayHandler {

    protected GuiTerminal gui;
    public List<ISimpleItemStack> stacks;

    public TerminalDisplayHandler(GuiTerminal gui) {
        this.gui = gui;
        this.stacks = Lists.newArrayList();
    }

    public void sort() {
        if (gui.tile.getControllerEntity() != null) {
            stacks.sort((left, right) -> right.getName().compareTo(left.getName()));
        }

        this.gui.updateScrollbar();
    }

    public void setStacks(List<ISimpleItemStack> simpleStacksList) {
        stacks.clear();

        loop:
        for (ISimpleItemStack stack : simpleStacksList) {
            for (ISimpleItemStack listStack : stacks) {
                if(ItemStack.areEqual(stack.getStack(), listStack.getStack())) {
                    listStack.addAmount(stack.getAmount());
                    continue loop;
                }
            }

            if(!stack.doesDisplayText()) {
                stacks.add(stack);
            }
        }
    }
}
