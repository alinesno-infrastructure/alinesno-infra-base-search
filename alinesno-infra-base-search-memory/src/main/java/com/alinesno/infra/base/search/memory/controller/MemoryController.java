package com.alinesno.infra.base.search.memory.controller;

import com.alinesno.infra.base.search.entity.VectorDatasetEntity;
import com.alinesno.infra.base.search.memory.BaseMemoryStore;
import com.alinesno.infra.base.search.memory.bean.MemoryNode;
import com.alinesno.infra.base.search.service.IVectorDatasetService;
import com.alinesno.infra.common.facade.pageable.DatatablesPageBean;
import com.alinesno.infra.common.facade.pageable.TableDataInfo;
import com.alinesno.infra.common.web.adapter.rest.BaseController;
import com.alinesno.infra.common.web.adapter.rest.SuperController;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@Scope("prototype")
@RequestMapping("/api/infra/base/search/memory")
public class MemoryController extends SuperController {

    @Resource
    private BaseMemoryStore memoryStore;

    /**
     * 获取ApplicationEntity的DataTables数据
     *
     * @param request HttpServletRequest对象
     * @param page    DatatablesPageBean对象
     * @return 包含DataTables数据的TableDataInfo对象
     */
    @ResponseBody
    @PostMapping("/datatables")
    public TableDataInfo datatables(HttpServletRequest request, DatatablesPageBean page) {

        TableDataInfo tableDataInfo = new TableDataInfo();

        List<MemoryNode> memoryNodes = List.of(
                MemoryNode.builder()
                        .memoryId("node1")
                        .userName("Alice")
                        .targetName("伴侣")
                        .content("那是2020年春天的一个周末，我和伴侣决定去附近的公园散步。那天阳光明媚，空气中弥漫着淡淡的花香。我们手牵手漫步在樱花树下，分享了彼此的梦想和未来的计划。这次约会让我感到无比幸福，仿佛时间都停止了。")
                        .keys("浪漫,初次见面")
                        .memoryType("event")
                        .timestamp(Instant.parse("2020-03-01T00:00:00Z").getEpochSecond())
                        .build(),
                MemoryNode.builder()
                        .memoryId("node2")
                        .userName("Bob")
                        .targetName("电影")
                        .content("这部电影讲述了一个关于勇气和友谊的故事，深深打动了我的心。主角面对重重困难从不放弃的精神给了我很大的鼓舞。看完后我久久不能平静，它不仅是一部电影，更是对我人生的一种启示。")
                        .keys("电影,感动")
                        .memoryType("note")
                        .timestamp(Instant.parse("2018-07-15T00:00:00Z").getEpochSecond())
                        .build(),
                MemoryNode.builder()
                        .memoryId("node3")
                        .userName("Charlie")
                        .targetName("玩具")
                        .content("这是我小时候最喜欢的玩具——一个可以变形的小汽车。每次玩它的时候，我都感觉自己进入了一个充满想象力的世界。它可以变成飞机、轮船甚至机器人，陪伴我度过了无数个快乐的时光。现在看来，这件玩具不仅仅是一个物件，更是童年的象征。")
                        .keys("怀旧,童年")
                        .memoryType("thought")
                        .timestamp(Instant.parse("1995-06-22T00:00:00Z").getEpochSecond())
                        .build(),
                MemoryNode.builder()
                        .memoryId("node4")
                        .userName("David")
                        .targetName("旅行")
                        .content("那次旅行是我第一次独自出国，目的地是意大利。我参观了许多历史遗迹，品尝了正宗的意式美食，还结识了一群来自世界各地的朋友。这次经历不仅开阔了我的视野，也增强了我的自信心，让我明白了世界之大，有太多值得探索的地方。")
                        .keys("旅游,冒险")
                        .memoryType("plan")
                        .timestamp(Instant.parse("2022-09-10T00:00:00Z").getEpochSecond())
                        .build(),
                MemoryNode.builder()
                        .memoryId("node5")
                        .userName("Eve")
                        .targetName("大学生活")
                        .content("大学生活充满了挑战与机遇。我不仅学到了专业知识，还在社团活动中锻炼了自己的领导能力。记得有一次参加辩论赛，虽然准备过程很辛苦，但最终获得了冠军，那种成就感至今难以忘怀。这段时光教会了我如何更好地管理时间和团队合作的重要性。")
                        .keys("青春,学习")
                        .memoryType("reminder")
                        .timestamp(Instant.parse("2016-09-01T00:00:00Z").getEpochSecond())
                        .build(),
                MemoryNode.builder()
                        .memoryId("node6")
                        .userName("Alice")
                        .targetName("家庭聚会")
                        .content("每年春节的家庭聚会总是充满了欢声笑语。长辈们围坐在一起包饺子，孩子们则在一旁嬉戏玩耍。饭桌上摆满了各种美味佳肴，大家一边吃一边聊天，分享过去一年的经历。这样的时刻让人感到家的温暖，也是维系亲情的重要纽带。")
                        .keys("家庭,节日")
                        .memoryType("event")
                        .timestamp(Instant.parse("2019-12-25T00:00:00Z").getEpochSecond())
                        .build(),
                MemoryNode.builder()
                        .memoryId("node7")
                        .userName("Bob")
                        .targetName("音乐会")
                        .content("那场音乐会在一个古老的剧院举行，现场氛围令人难以忘怀。当灯光暗下，舞台上的乐队开始演奏时，整个空间仿佛被音乐填满。每一首曲子都像是一段旅程，带领观众穿越不同的时空。最后的安可曲结束后，全场响起了雷鸣般的掌声，那一刻我感受到了音乐的力量。")
                        .keys("音乐,激情")
                        .memoryType("note")
                        .timestamp(Instant.parse("2021-04-18T00:00:00Z").getEpochSecond())
                        .build(),
                MemoryNode.builder()
                        .memoryId("node8")
                        .userName("Charlie")
                        .targetName("宠物")
                        .content("我的宠物狗Max总是在我最需要的时候给我安慰和支持。无论是一天工作的疲惫还是遇到挫折时的沮丧，只要回到家看到它摇着尾巴迎接我，所有的烦恼都会烟消云散。我们一起散步、玩耍，它成为了我生活中不可或缺的一部分。")
                        .keys("宠物,爱")
                        .memoryType("thought")
                        .timestamp(Instant.parse("2017-05-03T00:00:00Z").getEpochSecond())
                        .build(),
                MemoryNode.builder()
                        .memoryId("node9")
                        .userName("David")
                        .targetName("工作成就")
                        .content("去年底，我们的团队成功完成了一个重要项目，这对我来说是一个巨大的成就。在这个过程中，我克服了许多技术难题，学会了如何有效沟通和协调资源。当项目正式上线并得到客户好评时，那种成就感无法用言语来形容。这段经历不仅提升了我的专业技能，也为未来的职业发展奠定了坚实的基础。")
                        .keys("工作,成功")
                        .memoryType("plan")
                        .timestamp(Instant.parse("2022-11-12T00:00:00Z").getEpochSecond())
                        .build(),
                MemoryNode.builder()
                        .memoryId("node10")
                        .userName("Eve")
                        .targetName("阅读心得")
                        .content("最近读了一本名为《人类简史》的书，它以全新的视角解读了人类社会的发展历程。书中提到的一些观点改变了我对世界的认知，比如农业革命对人类生活方式的影响以及现代科技带来的伦理问题。通过这本书，我意识到历史不仅仅是过去的事情，它同样影响着我们的现在和未来。")
                        .keys("书籍,启发")
                        .memoryType("reminder")
                        .timestamp(Instant.parse("2020-08-20T00:00:00Z").getEpochSecond())
                        .build(),
                // 继续添加更多节点...
//                MemoryNode.builder()
//                        .memoryId("node11")
//                        .userName("Alice")
//                        .targetName("毕业典礼")
//                        .content("2023年的夏天，我参加了大学毕业典礼。看着台上的校长颁发学位证书，回想起四年来的点点滴滴，心中感慨万千。那些日夜苦读的日子、与室友一起庆祝生日的夜晚，还有无数次小组讨论，都成为了一段珍贵的记忆。这一刻标志着一个阶段的结束，同时也是新生活的开始。")
//                        .keys("毕业,庆祝")
//                        .memoryType("event")
//                        .timestamp(Instant.parse("2023-06-15T00:00:00Z").getEpochSecond())
//                        .build(),
//                MemoryNode.builder()
//                        .memoryId("node12")
//                        .userName("Bob")
//                        .targetName("运动比赛")
//                        .content("去年国庆节期间，我有幸观看了全国运动会的篮球决赛。两支队伍实力相当，比赛异常激烈。每当比分交替上升时，全场观众的情绪也被调动起来。最终，主队凭借最后几秒的关键投篮赢得了胜利，那一刻全场沸腾了。这场赛事不仅展现了运动员们的拼搏精神，也让我深刻体会到体育的魅力。")
//                        .keys("体育,比赛")
//                        .memoryType("note")
//                        .timestamp(Instant.parse("2022-10-01T00:00:00Z").getEpochSecond())
//                        .build(),
//                MemoryNode.builder()
//                        .memoryId("node13")
//                        .userName("Charlie")
//                        .targetName("烹饪课程"
//                        )
//                        .content("去年夏天，我报名参加了一个为期两周的法式烹饪课程。每天早上八点开始上课，老师会先讲解当天要做的菜品及其背后的文化背景，然后我们就在厨房里动手实践。从挑选食材到掌握火候，每一步都充满了乐趣和挑战。课程结束时，我已经能够独立制作几道经典的法国菜，这段经历不仅提高了我的厨艺，还让我对法国文化有了更深的理解。")
//                        .keys("烹饪,学习")
//                        .memoryType("thought")
//                        .timestamp(Instant.parse("2021-08-12T00:00:00Z").getEpochSecond())
//                        .build(),
//                MemoryNode.builder()
//                        .memoryId("node14")
//                        .userName("David")
//                        .targetName("摄影展览")
//                        .content("前年春天，我去参观了一个大型摄影展览。展厅内布置得非常精致，每一张照片都是摄影师用心捕捉的瞬间。从城市夜景到自然风光，从人物肖像到抽象艺术，这些作品让我看到了不同的视角和表达方式。特别是有一张拍摄于喜马拉雅山脉的照片，那片洁白无瑕的雪景仿佛把我带到了另一个世界。这次展览不仅是一次视觉盛宴，也激发了我对摄影的兴趣。")
//                        .keys("艺术,摄影")
//                        .memoryType("plan")
//                        .timestamp(Instant.parse("2020-05-20T00:00:00Z").getEpochSecond())
//                        .build(),
                MemoryNode.builder()
                        .memoryId("node15")
                        .userName("Eve")
                        .targetName("朋友婚礼")
                        .content("去年夏天，我参加了好友小美的婚礼。婚礼现场布置得温馨而浪漫，空气中弥漫着鲜花的香气。当新娘穿着白色婚纱缓缓走向新郎时，所有人的目光都被吸引住了。仪式结束后，宾客们共同举杯祝福这对新人，现场充满了欢笑和泪水。这次婚礼不仅是两个人的结合，更是两个家庭的融合，见证了爱情的美好。")
                        .keys("婚礼,庆祝")
                        .memoryType("reminder")
                        .timestamp(Instant.parse("2019-07-22T00:00:00Z").getEpochSecond())
                        .build(),
                MemoryNode.builder()
                        .memoryId("node16")
                        .userName("Alice")
                        .targetName("生日派对"
                        )
                        .content("去年四月，我举办了一场小型生日派对。邀请了几位亲密的好友来家里做客，大家一起准备食物、装饰房间，还玩了很多有趣的游戏。当蛋糕端上来，大家唱起生日歌时，那种温馨的感觉让我非常感动。这个派对不仅让我度过了愉快的一天，也加深了我们之间的友谊。")
                        .keys("生日,派对")
                        .memoryType("event")
                        .timestamp(Instant.parse("2022-04-10T00:00:00Z").getEpochSecond())
                        .build(),
                MemoryNode.builder()
                        .memoryId("node17")
                        .userName("Bob")
                        .targetName("科技讲座"
                        )
                        .content("去年三月，我参加了一场关于人工智能发展的讲座。演讲者是一位知名学者，他深入浅出地介绍了AI技术的最新进展，并预测了未来可能的应用场景。听完讲座后，我对这一领域产生了浓厚的兴趣，也开始关注相关领域的动态。这次讲座不仅拓宽了我的知识面，也让我对未来充满期待。")
                        .keys("科技,知识")
                        .memoryType("note")
                        .timestamp(Instant.parse("2021-03-05T00:00:00Z").getEpochSecond())
                        .build(),
                MemoryNode.builder()
                        .memoryId("node18")
                        .userName("Charlie")
                        .targetName("志愿服务"
                        )
                        .content("前年冬天，我参与了一次社区组织的志愿服务活动。我们帮助孤寡老人打扫卫生、采购日常用品，并陪他们聊天解闷。尽管天气寒冷，但老人们脸上洋溢的笑容让这个冬天变得格外温暖。这次经历让我明白，小小的善举也能带来巨大的改变，也让我更加珍惜身边的人。")
                        .keys("志愿,服务")
                        .memoryType("thought")
                        .timestamp(Instant.parse("2020-11-30T00:00:00Z").getEpochSecond())
                        .build(),
                MemoryNode.builder()
                        .memoryId("node19")
                        .userName("David")
                        .targetName("实习经历"
                        )
                        .content("大二暑假，我在一家互联网公司进行了为期两个月的实习。这段经历让我初步了解了职场环境和工作流程，学会了如何与同事协作解决问题。虽然有时会遇到困难，但在导师的帮助下，我逐渐适应并成长。这段实习经历不仅提升了我的专业技能，也为日后找工作积累了宝贵的经验。")
                        .keys("实习,经验")
                        .memoryType("plan")
                        .timestamp(Instant.parse("2019-09-15T00:00:00Z").getEpochSecond())
                        .build()) ;

        tableDataInfo.setRows(memoryNodes);
        tableDataInfo.setTotal(102);

        return tableDataInfo;
    }
}

