package nerdhub.simplestoragesystems.client.gui.widget;

import com.google.common.collect.Lists;
import nerdhub.simplestoragesystems.api.item.ICustomStorageStack;
import nerdhub.simplestoragesystems.client.gui.gui.GuiTerminal;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TerminalDisplayHandler {

    protected GuiTerminal gui;
    public List<ICustomStorageStack> stacks;

    public TerminalDisplayHandler(GuiTerminal gui) {
        this.gui = gui;
        this.stacks = Lists.newArrayList();
    }

    public void sort() {
        if (gui.tile.isLinked()) {
            stacks.sort((left, right) -> right.getName().compareTo(left.getName()));
        }

        this.gui.updateScrollbar();
    }

    public void setStacks(List<ICustomStorageStack> simpleStacksList) {
        stacks.clear();

        loop:
        for (ICustomStorageStack stack : simpleStacksList) {
            for (ICustomStorageStack listStack : stacks) {
                if (ItemStack.areEqual(stack.getStack(), listStack.getStack())) {
                    listStack.addAmount(stack.getAmount());
                    continue loop;
                }
            }

            if (!stack.isCraftingObject() && !stacks.contains(stack)) {
                stacks.add(stack);
            }
        }
    }

    //Copied from ItemStack and modified to not include stack size
    public boolean areEqual(ItemStack stack1, ItemStack stack2) {
        if (stack1.isEmpty() && stack2.isEmpty()) {
            return true;
        } else {
            return !stack1.isEmpty() && !stack2.isEmpty() ? isEqual(stack1, stack2) : false;
        }
    }

    public boolean isEqual(ItemStack stack1, ItemStack stack2) {
        if (stack1.getItem() != stack2.getItem()) {
            return false;
        } else if (stack1.getTag() == null && stack2.getTag() != null) {
            return false;
        } else {
            return stack1.getTag() == null || stack1.getTag().equals(stack2.getTag());
        }
    }
}
