package Array;

/**
 * There are n dominoes in a line, and we place each domino vertically upright. In the beginning, we simultaneously push some of the dominoes either to the left or to the right.
 * After each second, each domino that is falling to the left pushes the adjacent domino on the left. Similarly, the dominoes falling to the right push their adjacent dominoes standing on the right.
 * When a vertical domino has dominoes falling on it from both sides, it stays still due to the balance of the forces.
 * Return a string representing the final state.
 */
public class PushDominoes {

  public static void main(String[] args) {
    String dominoes = ".L.R...LR..L..";
    String result = pushDominoes(dominoes);
    System.out.println(result);
  }


  public static String pushDominoes(String dominoes) {
    char[] dominoesArr = dominoes.toCharArray();
    for (int i = 0, lastLeft = -1, lastRight = -1; i <= dominoes.length(); i++)
      if (i == dominoesArr.length || dominoesArr[i] == 'R') {
        if (lastRight > lastLeft) { // R.....R
          while (lastRight < i) {
            dominoesArr[lastRight++] = 'R';
          }
        }else {// L....R
          // do nothing
        }
        lastRight = i;
      } else if (dominoesArr[i] == 'L') {
        if (lastLeft > lastRight || lastRight == -1) { // L....L
          while (++lastLeft < i)
            dominoesArr[lastLeft] = 'L';
        } else { //R...L
          lastLeft = i;
          for (int lo = lastRight + 1, hi = lastLeft - 1; lo < hi; ) {//one in the middle stays '.'
            dominoesArr[lo++] = 'R';
            dominoesArr[hi--] = 'L';
          }
        }
      }
    return new String(dominoesArr);
  }

  public String pushDominoes2(String dominoes) {
    char[] dominoesArr = dominoes.toCharArray();
    int len = dominoesArr.length;
    int[] forces = new int[len];

    int force = 0;
    for(int i=0; i<len; i++) {
      if(dominoesArr[i] == 'R') {
        force = len;
      }else if (dominoesArr[i] == 'L') {
        force = 0;
      }else{
        force = Math.max(force-1, 0);
      }
      forces[i] += force;
    }

    force = 0;
    for(int i=len-1; i>=0; i--) {
      if(dominoesArr[i] == 'L') {
        force = len;
      }else if(dominoesArr[i] == 'R'){
        force = 0;
      }else{
        force = Math.max(force-1, 0);
      }
      forces[i] -= force;
    }

    StringBuilder builder = new StringBuilder();
    for(int i=0; i<forces.length; i++) {
      builder.append(forces[i] > 0 ? 'R' : forces[i] < 0 ? 'L' : '.');
    }
    return builder.toString();


  }

}
