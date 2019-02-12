package nerdhub.simplestoragesystems.client.gui.widget;

import nerdhub.simplestoragesystems.api.ISimpleItemStack;
import nerdhub.simplestoragesystems.client.gui.gui.GuiTerminal;

import java.util.*;

public class TerminalViewHelper {

    private GuiTerminal gui;

    private List<ISimpleItemStack> stacks = new ArrayList<>();
    protected Map<Integer, ISimpleItemStack> map = new HashMap<>();

    public TerminalViewHelper(GuiTerminal gui) {
        this.gui = gui;
    }

    public List<ISimpleItemStack> getStacks() {
        return stacks;
    }

    public void sort() {
        List<ISimpleItemStack> stacks = new ArrayList<>();

        if (gui.tile.isLinked) {
            stacks.addAll(map.values());

            Iterator<ISimpleItemStack> it = stacks.iterator();

            while (it.hasNext()) {
                ISimpleItemStack stack = it.next();
            }

            stacks.sort((left, right) -> right.getName().compareTo(left.getName()));
        }

        this.stacks = stacks;

        this.gui.updateScrollbar();
    }

    public void setStacks() {
        map.clear();

        for (ISimpleItemStack stack : stacks) {
            if (stack.doesDisplayText() && map.containsKey(stack.getHash())) {
                continue;
            }

            map.put(stack.getHash(), stack);
        }
    }
}
