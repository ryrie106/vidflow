import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import Swiper from 'react-id-swiper';
import './Home.css';
import 'react-id-swiper/src/styles/css/swiper.css';

const axios = require('axios');
const root = '/api';

class Home extends Component {

    constructor(props) {
        super(props);
        this.state = {posts: []};
    }

    componentDidMount() {
        axios.get('/api/posts').then(response => {
                this.setState({posts: response.data._embedded.posts});
        });
    }


    render() {

        return (
            <PostList postlist={this.state.posts}/>
        )
    }
}

class PostList extends Component {

    render() {
        const params = {
            direction: 'vertical',
            shouldSwiperUpdate: true,
        };

        const postList = this.props.postlist.map(post =>
            <Post key={post._links.self.href} post={post} />
        );

        return(
            <div className="wrapper">
                <Swiper {...params}>
                    {postList}
                </Swiper>
            </div>
        )
    }
}

class Post extends Component {

    render() {
        return (
            <div className="post">
                postno : {this.props.post.postno}<br/>
                writer : {this.props.post.writer}<br/>
                content : {this.props.post.content}<br/>
                numcomment : {this.props.post.numcomment}<br/>
                videosrc : {this.props.post.videosrc}<br/>
                regdate : {this.props.post.regdate}<br/>
                updatedate : {this.props.post.updatedate}<br/>
            </div>
        )
    }
}

export default Home;