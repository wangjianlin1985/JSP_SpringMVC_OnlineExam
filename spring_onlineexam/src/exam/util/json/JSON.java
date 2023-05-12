// 
// 
// 

package exam.util.json;

public abstract class JSON
{
    private char brace;
    protected StringBuffer json;
    
    public JSON() {
        this.brace = this.getBrace();
        this.json = new StringBuffer(new Character(this.brace).toString());
    }
    
    protected abstract char getBrace();
    
    public JSON addElement(final String key, final String value) {
        throw new UnsupportedOperationException();
    }
    
    public JSON addElement(final String key, final JSON object) {
        throw new UnsupportedOperationException();
    }
    
    public JSON addObject(final JSON json) {
        throw new UnsupportedOperationException();
    }
    
    private void appendTail() {
        final char tail = this.json.charAt(this.json.length() - 1);
        if (tail == ',') {
            this.json.deleteCharAt(this.json.length() - 1);
        }
        final char rightBrace = (this.brace == '{') ? '}' : ']';
        if (tail != rightBrace) {
            this.json.append(rightBrace);
        }
    }
    
    @Override
    public String toString() {
        this.appendTail();
        return this.json.toString();
    }
    
    public void clear() {
        this.json.delete(1, this.json.length());
    }
}
