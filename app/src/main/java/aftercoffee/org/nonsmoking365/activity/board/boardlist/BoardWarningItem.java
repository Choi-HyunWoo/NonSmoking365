package aftercoffee.org.nonsmoking365.activity.board.boardlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-09.
 */
public class BoardWarningItem implements BoardItem {
    String _id;         // 글의 _id
    int titleImg;
    String title;
    String contents;

    boolean likeOn;
    int likesCount;
    int commentsCount;
}
