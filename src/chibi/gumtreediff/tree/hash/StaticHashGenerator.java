/*
 * This file is part of GumTree.
 *
 * GumTree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GumTree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GumTree.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011-2015 Jean-Rémy Falleri <jr.falleri@gmail.com>
 * Copyright 2011-2015 Floréal Morandat <florealm@gmail.com>
 */

package chibi.gumtreediff.tree.hash;

import static chibi.gumtreediff.tree.hash.HashUtils.*;

import chibi.gumtreediff.tree.ITree;
import chibi.gumtreediff.tree.ITree;

public abstract class StaticHashGenerator implements HashGenerator {

    public void hash(ITree t) {
        for (ITree n: t.postOrder())
            n.setHash(nodeHash(n));
    }

    public abstract int nodeHash(ITree t);

    public static class StdHashGenerator extends StaticHashGenerator {

        @Override
        public int nodeHash(ITree t) {
            return t.toStaticHashString().hashCode();
        }

    }

    public static class Md5HashGenerator extends StaticHashGenerator {

        @Override
        public int nodeHash(ITree t) {
            return HashUtils.md5(t.toStaticHashString());
        }

    }

}
