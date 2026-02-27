# Application Load Balancer for SonarQube

resource "aws_lb" "sonarqube" {
  count = var.auto_scaling_enabled ? 1 : 0

  name               = "${var.project_name}-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.sonarqube.id]
  subnets            = aws_subnet.public[*].id

  enable_deletion_protection = false

  tags = {
    Name = "${var.project_name}-alb"
  }
}

# Target Group
resource "aws_lb_target_group" "sonarqube" {
  count = var.auto_scaling_enabled ? 1 : 0

  name     = "${var.project_name}-tg"
  port     = 9000
  protocol = "HTTP"
  vpc_id   = aws_vpc.main.id

  health_check {
    path                = "/api/system/status"
    healthy_threshold   = 2
    unhealthy_threshold = 3
    timeout             = 5
    interval            = 30
    matcher             = "200"
  }

  tags = {
    Name = "${var.project_name}-tg"
  }
}

# ALB Listener
resource "aws_lb_listener" "sonarqube" {
  count = var.auto_scaling_enabled ? 1 : 0

  load_balancer_arn = aws_lb.sonarqube[0].arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.sonarqube[0].arn
  }
}
