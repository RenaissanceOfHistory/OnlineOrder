<div class="container">
    <header>
        <h1>网上订餐系统</h1>
        <h3>参考哔哩哔哩网站视频项目</h3>
    </header>
    <section>
        <article>
            <p>所述参考内容为：数据库表的字段名，项目页面设计以及一些功能参考。</p>    
            <p>本项目并不是完全的照搬，因为所有资源均有编者自己查找，视频中使用了许多页面，
                而编者则较多使用Thymeleaf的模板功能，本想着创建类似Vue那样的单页面应用，
                但由于缺少经验，只对功能栏的功能进行组件拆分。</p>
            <p>在数据的展示上，使用了表格和卡片两种显示方式，这使用了媒体查询的CSS来进行调节。
                而在数据的请求方面，较多使用了Jquery进行异步通信，且使用wait-to-js解决异常回调。</p>
        </article>
        <article>
            <h5>项目功能</h5>
            <ul>
                <li>用户管理(管理员/普通用户)</li>
                <li>菜品管理（由于视频项目没有最初没有讲述，有些地方写为了"食品"）</li>
                <li>店铺管理</li>
                <li>数据统计（使用chart.js）</li>
                <li>订单管理</li>
                <li>...</li>
            </ul>
        </article>
    </section>
</div>