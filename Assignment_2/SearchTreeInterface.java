/*
 *  SearchTreeInterface provides the structure for the SearchTree class
 */
public interface SearchTreeInterface {
	 /**
     * The function adds a node in the tree
     *
     * @param   key  Key to be added
     * @return       boolean
     */
    boolean add(String key);

    /**
     * The functions searches for the key in the tree and if found, the search count is incremented.
     * Items are moved with their parent nodes on the basis of the search count
     * so that frequently searched item can be accessed easily.
     * 
     * @param    key    key to search
     * @return          int (depth of node)
     */
    int find(String key);

    /**
     * Reset all the search counts in this tree to 0
     */
    void reset();

    /**
     * Prints the tree
     *
     * @return string representation of this tree.
     */
    String printTree();

}
